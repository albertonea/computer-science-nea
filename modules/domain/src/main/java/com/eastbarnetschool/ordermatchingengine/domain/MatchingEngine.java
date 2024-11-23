package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.Optional;
import java.util.PriorityQueue;

import static com.eastbarnetschool.ordermatchingengine.domain.Side.BUY;

public class MatchingEngine {
    private OrderBook orderBook;
    private String ticker;

    public MatchingEngine(String ticker) {
        this.ticker = ticker;
        orderBook = new OrderBook();
    }

    public Optional<OrderBook> getOrderBook() {
        return Optional.ofNullable(orderBook);
    }

    public void placeOrder(Order order) {
        if (order.getSide() == BUY) {
            tryToMatchBuyOrder(order);
        }
    }

    private void tryToMatchBuyOrder(Order order) {
        PriorityQueue<PriceLevel> sellSide = orderBook.getSellSide();
        while (!sellSide.isEmpty() && order.getQuantity() > 0) {
            PriceLevel bestPriceLevel = sellSide.peek();
            if (bestPriceLevel.getPrice() <= order.getPrice()) {
                for (Order bestPriceLevelOrder : bestPriceLevel.getOrders()) {
                    if (bestPriceLevelOrder.getQuantity() <= order.getQuantity()) {
                        order.fill(bestPriceLevelOrder.getQuantity());
                        bestPriceLevel.getOrders().remove(bestPriceLevelOrder);
                    } else {
                        order.fill(bestPriceLevelOrder.getQuantity());
                        bestPriceLevelOrder.fill();
                    }
                }

            }
        }
    }
}