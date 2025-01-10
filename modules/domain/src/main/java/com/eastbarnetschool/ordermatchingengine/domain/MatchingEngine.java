package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.ArrayList;

import static com.eastbarnetschool.ordermatchingengine.domain.OrderType.LIMIT;

public class MatchingEngine {
    private OrderBook orderBook;
    private String ticker;

    public MatchingEngine(String ticker) {
        this.ticker = ticker;
        orderBook = new OrderBook();
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public ArrayList<Trade> placeOrder(Order order) {
        if (order.getOrderType() == LIMIT) {
            ArrayList<Trade> trades = orderBook.placeLimitOrder(order);
            System.out.println("trades: " + trades);
            System.out.println("buy side");
            System.out.println(orderBook.getBuySide());
            System.out.println("sell side");
            System.out.println(orderBook.getSellSide());
            return trades;
        }
//        else if (order.getOrderType() == MARKET) {
//            ArrayList<Trade> trades = orderBook.placeMarketOrder(order);
//
//            System.out.println("trades: " + trades);
//        }
        else {
            throw new IllegalArgumentException("Order type sent to place limit order: " + order.getOrderType());
        }

    }


}