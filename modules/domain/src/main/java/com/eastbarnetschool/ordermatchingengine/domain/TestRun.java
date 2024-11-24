package com.eastbarnetschool.ordermatchingengine.domain;

public class TestRun {
    public static void main(String[] args) {
        OrderGateway orderGateway = new OrderGateway();
        orderGateway.placeOrder(new Order(100.0, 10, "AAPL", Side.BUY, OrderType.LIMIT));
        orderGateway.placeOrder(new Order(100.0, 10, "AAPL", Side.BUY, OrderType.LIMIT));
        orderGateway.placeOrder(new Order(100.0, 5, "AAPL", Side.SELL, OrderType.MARKET));
    }
}
