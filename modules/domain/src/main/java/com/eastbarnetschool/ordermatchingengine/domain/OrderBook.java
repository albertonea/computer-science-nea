package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class OrderBook {
    private PriorityQueue<PriceLevel> sellSide;
    private PriorityQueue<PriceLevel> buySide;

    public OrderBook() {
        sellSide = new PriorityQueue<>((a, b) -> Float.compare(b.getPrice(), a.getPrice()));
        buySide = new PriorityQueue<>((a, b) -> Float.compare(a.getPrice(), b.getPrice()));
    }

    public PriorityQueue<PriceLevel> getBuySide() {
        return buySide;
    }

    public PriorityQueue<PriceLevel> getSellSide() {
        return sellSide;
    }

    public Boolean canMatch(Side side, Long price) {
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

    public ArrayList<Trade> placeLimitOrder(Order order) {
        ArrayList<Trade> trades = new ArrayList<>();
        while (canMatch(order.getSide(), order.getPrice()) && !order.isFilled()) {
            LinkedList<Order> bookOrders;
            if (order.getSide() == Side.BUY) {
                bookOrders = sellSide.peek().getOrders();
            } else {
                bookOrders = buySide.peek().getOrders();
            }

            while (!bookOrders.isEmpty()) {
                Order bookOrder = bookOrders.peek();
                Integer quantity = Math.min(bookOrder.getRemainingQuantity(), order.getRemainingQuantity());
                bookOrder.fill(quantity);
                order.fill(quantity);

                if (order.isFilled() && bookOrder.isFilled()) {
                    bookOrders.removeFirst();
                    trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                    break;
                } else if (order.isFilled()) {
                    trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                    break;
                } else if (bookOrder.isFilled()) {
                    trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                    bookOrders.removeFirst();
                } else {
                    throw new IllegalStateException("Neither order got fully filled");
                }
            }

            if (bookOrders.isEmpty()) {
                if (order.getSide() == Side.BUY) {
                    sellSide.poll();
                } else {
                    buySide.poll();
                }
            }
        }

        if (!order.isFilled()) {
            if (order.getSide() == Side.BUY) {
                for (PriceLevel level : buySide) {
                    if (Math.abs(level.getPrice() - order.getPrice()) < 0.0001 ) {
                        level.addOrder(order);
                    }
                }
                buySide.add(new PriceLevel(order.getPrice(), order));
            } else {
                for (PriceLevel level : sellSide) {
                    if (Math.abs(level.getPrice() - order.getPrice()) < 0.0001 ) {
                        level.addOrder(order);
                    }
                }
                sellSide.add(new PriceLevel(order.getPrice(), order));
            }
        }

        return trades;
    }

    public ArrayList<Trade> placeMarketOrder(Order order) {
        ArrayList<Trade> trades = new ArrayList<>();
        if (order.getSide() == Side.BUY) {
            while (!sellSide.isEmpty()) {
                LinkedList<Order> bookOrders = sellSide.peek().getOrders();

                while (!bookOrders.isEmpty()) {
                    Order bookOrder = bookOrders.peek();
                    Integer quantity = Math.min(bookOrder.getRemainingQuantity(), order.getRemainingQuantity());
                    bookOrder.fill(quantity);
                    order.fill(quantity);

                    if (order.isFilled() && bookOrder.isFilled()) {
                        bookOrders.removeFirst();
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                        break;
                    } else if (order.isFilled()) {
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                        break;
                    } else if (bookOrder.isFilled()) {
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                        bookOrders.removeFirst();
                    } else {
                        throw new IllegalStateException("Neither order got fully filled");
                    }
                }

                if (bookOrders.isEmpty()) {
                    sellSide.poll();
                }
                if (order.isFilled()) {
                    break;
                }
            }
        } else {
            while (!buySide.isEmpty()) {
                LinkedList<Order> bookOrders = buySide.peek().getOrders();

                while (!bookOrders.isEmpty()) {
                    Order bookOrder = bookOrders.peek();
                    Integer quantity = Math.min(bookOrder.getRemainingQuantity(), order.getRemainingQuantity());
                    bookOrder.fill(quantity);
                    order.fill(quantity);

                    if (order.isFilled() && bookOrder.isFilled()) {
                        bookOrders.removeFirst();
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                        break;
                    } else if (order.isFilled()) {
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                        break;
                    } else if (bookOrder.isFilled()) {
                        trades.add(new Trade(Instant.now(), bookOrder.getPrice(), quantity));
                        bookOrders.removeFirst();
                    } else {
                        throw new IllegalStateException("Neither order got fully filled");
                    }
                }

                if (bookOrders.isEmpty()) {
                    buySide.poll();
                }
                if (order.isFilled()) {
                    break;
                }
            }
        }
        return trades;
    }
}
