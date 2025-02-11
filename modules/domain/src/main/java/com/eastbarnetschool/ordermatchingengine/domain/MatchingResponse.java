package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

import java.util.List;

public class MatchingResponse {
    private final List<Order> filledOrders;
    private final List<Trade> trades;

    public MatchingResponse(List<Trade> trades, List<Order> filledOrders) {
        this.trades = trades;
        this.filledOrders = filledOrders;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public List<Order> getFilledOrders() {
        return filledOrders;
    }
}
