package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.LimitOrder;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

import java.util.List;

public class MatchingEngineResponse {
    private List<LimitOrder> filledOrders;
    private List<Trade> trades;
    private Order placedOrder;

    public MatchingEngineResponse(List<Trade> trades, List<LimitOrder> filledOrders, Order placedOrder) {
        this.trades = trades;
        this.filledOrders = filledOrders;
        this.placedOrder = placedOrder;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public Order getPlacedOrder() {
        return placedOrder;
    }

    public List<LimitOrder> getFilledOrders() {
        return filledOrders;
    }
}
