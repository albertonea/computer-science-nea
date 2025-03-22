package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.events.OrderFilledEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.TradeEvent;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.PriceUpdateListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderBook {
    private final PriorityQueue<PriceLevel> sellSide;
    private final PriorityQueue<PriceLevel> buySide;
    private final PriceUpdateListener priceUpdateListener;
    private final OrderGateway eventPublisher;
    private Long lastPrice;

    public OrderBook(PriceUpdateListener priceUpdateListener, OrderGateway eventPublisher) {
        // sell side queue with priority on price ascending
        sellSide = new PriorityQueue<>(PriorityQueueOrder.ASC);
        // buy side queue with priority on price descending
        buySide = new PriorityQueue<>(PriorityQueueOrder.DESC);
        lastPrice = Long.MAX_VALUE;
        this.priceUpdateListener = priceUpdateListener;
        this.eventPublisher = eventPublisher;
    }

    // function to check if an order can be matched
    // with any in the orderbook
    public Boolean canMatch(Order order) {
        // if it's a market order, checking the other side is not empty
        if (order.isMarketOrder()) {
            if (order.getSide() == Side.BUY) {
                return !sellSide.isEmpty();
            } else {
                return !buySide.isEmpty();
            }
        } else {
            // if it's a limit order check side is empty
            // and the prices can be matched
            if (order.getSide() == Side.BUY) {
                if (sellSide.isEmpty()) return false;
                PriceLevel sellSide = this.sellSide.peek();
                return sellSide.getPrice() <= order.getPrice();
            } else {
                if (buySide.isEmpty()) return false;
                PriceLevel buySide = this.buySide.peek();
                return buySide.getPrice() <= order.getPrice();
            }
        }
    }

    // function to place the order
    public void placeOrder(Order order) {
        // calls execute matching logic to match the order
        // returns trades and filled orders in the response
        MatchingResponse response = executeMatchingLogic(order);

        // loop through trades and send event
        for (Trade trade : response.getTrades()) {
            eventPublisher.publishTradeEvent(new TradeEvent(trade));
        }

        // loop through filled orders and send event
        for (Order filledOrder : response.getFilledOrders()) {
            eventPublisher.publishOrderFilledEvent(new OrderFilledEvent(filledOrder));
        }
    }

    // function to execute the matching logic
    public MatchingResponse executeMatchingLogic(Order order) {
        List<Trade> trades = new ArrayList<>();
        List<Order> filledOrders = new ArrayList<>();
        // keep looping until you can't match the order
        // or the order is filled
        while (canMatch(order) && !order.isFilled()) {
            //get the next best price level
            PriceLevel priceLevel;
            if (order.getSide() == Side.BUY) {
                priceLevel = sellSide.peek();
            } else {
                priceLevel = buySide.peek();
            }

            //get the price levels price
            Long price = priceLevel.getPrice();

            //loop until the price level is empty
            while (!priceLevel.isEmpty()) {
                Order bookOrder = priceLevel.peek();
                //get the smallest quantity from either the placed
                // order or the book order
                Long quantity = Math.min(bookOrder.getRemainingQuantity(), order.getRemainingQuantity());

                //fill both orders with this quantity
                bookOrder.fill(quantity, price);
                order.fill(quantity, price);


                if (order.isFilled() | bookOrder.isFilled()) {
                    //set new price
                    if (!Objects.equals(price, lastPrice)) setLastPrice(price);

                    //generate trades
                    if (order.getSide() == Side.BUY) {
                        trades.add(new Trade(Instant.now(), price, quantity, order, bookOrder, order.getTicker()));
                    } else {
                        trades.add(new Trade(Instant.now(), price, quantity, bookOrder, order, order.getTicker()));
                    }

                    //add to filled orders, break if the placed
                    //order is filled
                    if (bookOrder.isFilled() && order.isFilled()) {
                        filledOrders.add(order);
                        filledOrders.add(bookOrder);
                        priceLevel.dequeue();
                        break;
                    }

                    if (bookOrder.isFilled()) {
                        filledOrders.add(bookOrder);
                        priceLevel.dequeue();
                    }

                    if (order.isFilled()) {
                        filledOrders.add(order);
                        filledOrders.add(bookOrder);
                        break;
                    }
                } else {
                    throw new IllegalStateException("Neither order got fully filled");
                }
            }

            if (priceLevel.isEmpty()) {
                if (order.getSide() == Side.BUY) {
                    sellSide.dequeue();
                } else {
                    buySide.dequeue();
                }
            }
        }

        if (!order.isFilled()) {
            //order is not filled, and it's a limit order
            // add it to the ledger
            if (order.isLimitOrder()) {
                if (order.getInitialQuantity() > order.getRemainingQuantity()) {
                    filledOrders.add(order);
                }
                if (order.getSide() == Side.BUY) {
                    PriceLevel level = buySide.findByPriority(order.getPrice().intValue());
                    if (level != null) {
                        level.enqueue(order);
                        return new MatchingResponse(trades, filledOrders);
                    } else {
                        buySide.enqueue(new PriceLevel(order.getPrice(), order), order.getPrice().intValue());
                    }
                } else {
                    PriceLevel level = sellSide.findByPriority(order.getPrice().intValue());
                    if (level != null) {
                        level.enqueue(order);
                        return new MatchingResponse(trades, filledOrders);
                    } else {
                        sellSide.enqueue(new PriceLevel(order.getPrice(), order), order.getPrice().intValue());
                    }
                }
            }

            //if the order is a market order
            //it get treated as if it's been filled
            if (order.isMarketOrder()) {
                filledOrders.add(order);
            }
        }

        //return trades and filled orders
        return new MatchingResponse(trades, filledOrders);
    }

    private void notifyListeners() {
        priceUpdateListener.onPriceUpdated(lastPrice);
    }

    public void setLastPrice(Long price) {
        this.lastPrice = price;
        notifyListeners();
    }

    public Long getLastPrice() {
        return lastPrice;
    }
}
