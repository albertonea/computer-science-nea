package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.events.OrderFilledEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.TradeEvent;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.PriceUpdateListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

import java.time.Instant;
import java.util.*;

public class OrderBook {
    private final PriorityQueue<PriceLevel> sellSide;
    private final PriorityQueue<PriceLevel> buySide;
    private final PriceUpdateListener priceUpdateListener;
    private final OrderGateway eventPublisher;
    private Long lastPrice;

    public OrderBook(PriceUpdateListener priceUpdateListener, OrderGateway eventPublisher) {
        sellSide = new PriorityQueue<>(Comparator.comparingLong(PriceLevel::getPrice).reversed());
        buySide = new PriorityQueue<>(Comparator.comparingLong(PriceLevel::getPrice));
        lastPrice = Long.MAX_VALUE;
        this.priceUpdateListener = priceUpdateListener;
        this.eventPublisher = eventPublisher;
    }

    public Boolean canMatch(Order order) {
        if (order.isMarketOrder()) {
            if (order.getSide() == Side.BUY) {
                return !sellSide.isEmpty();
            } else {
                return !buySide.isEmpty();
            }
        } else {
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

    public void placeOrder(Order order) {
        MatchingResponse response = executeMatchingLogic(order);

        for (Trade trade : response.getTrades()) {
            eventPublisher.publishTradeEvent(new TradeEvent(trade));
        }

        for (Order filledOrder : response.getFilledOrders()) {
            eventPublisher.publishOrderFilledEvent(new OrderFilledEvent(filledOrder));
        }
    }

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
                        priceLevel.poll();
                        break;
                    }

                    if (bookOrder.isFilled()) {
                        filledOrders.add(bookOrder);
                        priceLevel.poll();
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
                    sellSide.poll();
                } else {
                    buySide.poll();
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
                    for (PriceLevel level : buySide) {
                        if (Objects.equals(level.getPrice(), order.getPrice())) {
                            level.addOrder(order);
                            return new MatchingResponse(trades, filledOrders);
                        }
                    }
                    buySide.add(new PriceLevel(order.getPrice(), order));
                } else {
                    for (PriceLevel level : sellSide) {
                        if (Objects.equals(level.getPrice(), order.getPrice())) {
                            level.addOrder(order);
                            return new MatchingResponse(trades, filledOrders);
                        }
                    }
                    sellSide.add(new PriceLevel(order.getPrice(), order));
                }
            }

            //if the order is a market order
            //it get treated as if its been filled
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
