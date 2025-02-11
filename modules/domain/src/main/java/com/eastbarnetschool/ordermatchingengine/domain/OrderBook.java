package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.listeners.PriceUpdateListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.LimitOrder;
import com.eastbarnetschool.ordermatchingengine.domain.orders.MarketOrder;

import java.time.Instant;
import java.util.*;

public class OrderBook {
    private final PriorityQueue<PriceLevel> sellSide;
    private final PriorityQueue<PriceLevel> buySide;
    private final PriceUpdateListener priceUpdateListener;
    private Long lastPrice;

    public OrderBook(PriceUpdateListener priceUpdateListener) {
        sellSide = new PriorityQueue<>(Comparator.comparingLong(PriceLevel::getPrice).reversed());
        buySide = new PriorityQueue<>(Comparator.comparingLong(PriceLevel::getPrice));
        lastPrice = Long.MAX_VALUE;
        this.priceUpdateListener = priceUpdateListener;
    }

    public Boolean canMatchLimitOrder(Side side, Long price) {
        if (side == Side.BUY) {
            if (sellSide.isEmpty()) return false;
            PriceLevel sellSide = this.sellSide.peek();
            return sellSide.getPrice() <= price;
        } else {
            if (buySide.isEmpty()) return false;
            PriceLevel buySide = this.buySide.peek();
            return buySide.getPrice() >= price;
        }
    }

    public Boolean canMatchMarketOrder(Side side, Long remainingQuantity) {
        if (side == Side.BUY) {
            if (sellSide.isEmpty()) return false;
            PriceLevel sellSide = this.sellSide.peek();
            return sellSide.getPrice() <= remainingQuantity;
        } else {
            return !buySide.isEmpty();
        }
    }

    public MatchingEngineResponse placeLimitOrder(LimitOrder order) {
        List<Trade> trades = new ArrayList<>();
        List<LimitOrder> filledOrders = new ArrayList<>();
        while (canMatchLimitOrder(order.getSide(), order.getPrice()) && !order.isFilled()) {
            PriceLevel priceLevel;
            if (order.getSide() == Side.BUY) {
                priceLevel = sellSide.peek();
            } else {
                priceLevel = buySide.peek();
            }

            while (!priceLevel.isEmpty()) {
                LimitOrder bookOrder = priceLevel.peek();
                Long quantity = Math.min(bookOrder.getRemainingQuantity(), order.getRemainingQuantity());
                bookOrder.fill(quantity);
                order.fill(quantity);

                if (order.isFilled() | bookOrder.isFilled()) {
                    setLastPrice(bookOrder.getPrice());
                    if (order.getSide() == Side.BUY) {
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity, order.getUserId(), bookOrder.getUserId(), order.getTicker()));
                    } else {
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity, bookOrder.getUserId(), order.getUserId(), order.getTicker()));
                    }

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
            if (order.getInitialQuantity() > order.getRemainingQuantity()) {
                filledOrders.add(order);
            }
            if (order.getSide() == Side.BUY) {
                for (PriceLevel level : buySide) {
                    if (Objects.equals(level.getPrice(), order.getPrice())) {
                        level.addOrder(order);
                        return new MatchingEngineResponse(trades, filledOrders, order);
                    }
                }
                buySide.add(new PriceLevel(order.getPrice(), order));
            } else {
                for (PriceLevel level : sellSide) {
                    if (Objects.equals(level.getPrice(), order.getPrice())) {
                        level.addOrder(order);
                        return new MatchingEngineResponse(trades, filledOrders, order);
                    }
                }
                sellSide.add(new PriceLevel(order.getPrice(), order));
            }
        }
        return new MatchingEngineResponse(trades, filledOrders, order);
    }

    public MatchingEngineResponse placeMarketOrder(MarketOrder order) {
        List<Trade> trades = new ArrayList<>();
        List<LimitOrder> filledOrders = new ArrayList<>();

        if (order.getSide() == Side.BUY) {
            while (canMatchMarketOrder(order.getSide(), order.getRemainingQuantity()) && !order.isFilled()) {
                PriceLevel priceLevel;
                if (order.getSide() == Side.BUY) {
                   priceLevel = sellSide.peek();
                } else {
                   priceLevel = buySide.peek();
                }

                while (!priceLevel.isEmpty()) {
                    Long price = priceLevel.getPrice();
                    LimitOrder bookOrder = priceLevel.peek();
                    Long quantity = Math.min((bookOrder.getRemainingQuantity()*bookOrder.getPrice()), (order.getRemainingQuantity()));
                    bookOrder.fill(quantity/price);
                    order.fill(quantity);

                    if (order.isFilled(price) | bookOrder.isFilled()) {
                        setLastPrice(bookOrder.getPrice());
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity, order.getUserId(), bookOrder.getUserId(), order.getTicker()));

                        if (bookOrder.isFilled() && order.isFilled(price)) {
                            filledOrders.add(bookOrder);
                            priceLevel.poll();
                            break;
                        }

                        if (bookOrder.isFilled()) {
                            filledOrders.add(bookOrder);
                            priceLevel.poll();
                        }

                        if (order.isFilled(price)) {
                            filledOrders.add(bookOrder);
                            break;
                        }
                    } else {
                        throw new IllegalStateException("Neither order got fully filled");
                    }
                }

                if (priceLevel.isEmpty()) {
                    sellSide.poll();
                }
                if (order.isFilled(priceLevel.getPrice())) {
                    break;
                }
            }
        } else {
            while (canMatchMarketOrder(order.getSide(), order.getRemainingQuantity()) && !order.isFilled()) {
                PriceLevel priceLevel;
                if (order.getSide() == Side.BUY) {
                    priceLevel = sellSide.peek();
                } else {
                    priceLevel = buySide.peek();
                }

                while (!priceLevel.isEmpty()) {
                    LimitOrder bookOrder = priceLevel.peek();
                    Long quantity = Math.min(bookOrder.getRemainingQuantity(), order.getRemainingQuantity());
                    bookOrder.fill(quantity);
                    order.fill(quantity);

                    if (order.isFilled() | bookOrder.isFilled()) {
                        setLastPrice(bookOrder.getPrice());
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity, bookOrder.getUserId(), order.getUserId(), order.getTicker()));

                        if (bookOrder.isFilled() && order.isFilled()) {
                            filledOrders.add(bookOrder);
                            priceLevel.poll();
                            break;
                        }

                        if (bookOrder.isFilled()) {
                            filledOrders.add(bookOrder);
                            priceLevel.poll();
                        }

                        if (order.isFilled()) {
                            filledOrders.add(bookOrder);
                            break;
                        }
                    } else {
                        throw new IllegalStateException("Neither order got fully filled");
                    }
                }

                if (priceLevel.isEmpty()) {
                    buySide.poll();
                }
                if (order.isFilled()) {
                    break;
                }
            }
        }
        return new MatchingEngineResponse(trades, filledOrders, order);
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
