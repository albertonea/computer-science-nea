package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.HashMap;

public class OrderGateway {
    private final HashMap<String, OrderQueue> orderQueues;

    public OrderGateway() {
        orderQueues = new HashMap<>();
    }

    public OrderQueue getOrderQueue(String ticker) {
        OrderQueue orderQueue = orderQueues.get(ticker);
        if (orderQueue == null) {
            OrderQueue newOrderQueue = new OrderQueue(ticker);
            orderQueues.put(ticker, newOrderQueue);
            return newOrderQueue;
        }
        return orderQueue;
    }

    public MatchingEngineResponse placeOrder(Order order) {
        return getOrderQueue(order.getTicker()).placeOrder(order);
    }
}
