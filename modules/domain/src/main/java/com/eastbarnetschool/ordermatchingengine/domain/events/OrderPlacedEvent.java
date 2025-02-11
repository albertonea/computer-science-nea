package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

public class OrderPlacedEvent {
    private final Order order;
    public OrderPlacedEvent(Order order) {
        this.order = order;
    }
    public Order getOrder() {
        return order;
    }
}
