package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.OrderFactory;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

import java.util.UUID;

public class main {
    public static void main(String[] args) {
        OrderGateway orderGateway = new OrderGateway();
        OrderFactory orderFactory = new OrderFactory();

        System.out.println("placing order");
        orderGateway.placeStopOrder(new StopOrder(orderFactory.createLimitOrder(2000L, 2000L, "AAPL", Side.SELL, UUID.randomUUID()), 1000L));
        MatchingEngineResponse response = orderGateway.placeOrder(orderFactory.createLimitOrder(1000L, 1000L, "AAPL", Side.BUY, UUID.randomUUID()));
        MatchingEngineResponse response1 = orderGateway.placeOrder(orderFactory.createMarketOrder(1000L, "AAPL", Side.SELL, UUID.randomUUID()));
        System.out.println(response);
        System.out.println(response1.getTrades());
        MatchingEngineResponse response2 = orderGateway.placeOrder(orderFactory.createMarketOrder(4000000L, "AAPL", Side.BUY, UUID.randomUUID()));
        System.out.println(response2.getTrades());
    }
}
