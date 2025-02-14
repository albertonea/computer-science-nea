package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

public class OrderFilledEvent {
    private final Order order;
    public OrderFilledEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
