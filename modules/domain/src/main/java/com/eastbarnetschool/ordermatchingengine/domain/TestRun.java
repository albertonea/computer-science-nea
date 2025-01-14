package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;
import java.util.UUID;

public class TestRun {
    public static void main(String[] args) {
        OrderGateway orderGateway = new OrderGateway();
        orderGateway.placeOrder(new Order((long) 100, 10, "AAPL", Side.BUY, OrderType.LIMIT, UUID.randomUUID(), Instant.now()));
        orderGateway.placeOrder(new Order((long) 100, 10, "AAPL", Side.SELL, OrderType.LIMIT, UUID.randomUUID(), Instant.now()));
    }
}
