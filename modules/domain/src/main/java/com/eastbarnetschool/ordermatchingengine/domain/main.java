package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;
import com.sun.security.auth.NTSidDomainPrincipal;

import java.time.Instant;
import java.util.UUID;

public class main {
    public static void main(String[] args) {
        Notifier notifier = new Notifier();
        OrderGateway orderGateway = new OrderGateway();
        orderGateway.addTradingEventListener(notifier);

        orderGateway.placeOrder(new Order(1000L, 1000L, 10000L, 0L,  "AAPL", Side.SELL, OrderType.LIMIT, UUID.randomUUID(), Instant.now()));
        orderGateway.placeOrder(new Order(1000L, 1000L, 10000L, 0L,  "AAPL", Side.BUY, OrderType.LIMIT, UUID.randomUUID(), Instant.now()));
        System.out.println("order placed");
    }
}
