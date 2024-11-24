package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.PriorityQueue;

import static com.eastbarnetschool.ordermatchingengine.domain.OrderType.LIMIT;
import static com.eastbarnetschool.ordermatchingengine.domain.OrderType.MARKET;
import static com.eastbarnetschool.ordermatchingengine.domain.Side.BUY;

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

    public void placeOrder(Order order) {
        if (order.getOrderType() == LIMIT) {
            ArrayList<Trade> trades = orderBook.placeLimitOrder(order);
        } else if (order.getOrderType() == MARKET) {
            orderBook.placeMarketOrder(order);
        } else {
            throw new IllegalArgumentException("Order type sent to place limit order: " + order.getOrderType());
        }
    }


}