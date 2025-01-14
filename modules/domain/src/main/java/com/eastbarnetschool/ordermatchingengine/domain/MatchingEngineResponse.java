package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.ArrayList;

public class MatchingEngineResponse {
    private ArrayList<Order> filledOrders;
    private ArrayList<Trade> trades;
    private Order placedOrder;

    public MatchingEngineResponse(ArrayList<Trade> trades, ArrayList<Order> filledOrders, Order placedOrder) {
        this.trades = trades;
        this.filledOrders = filledOrders;
        this.placedOrder = placedOrder;
    }

    public ArrayList<Trade> getTrades() {
        return trades;
    }

    public Order getPlacedOrder() {
        return placedOrder;
    }

    public ArrayList<Order> getFilledOrders() {
        return filledOrders;
    }
}
