package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

// OrderEvent.java
public class OrderFilledEvent {
    private final Order order;
    // You can add additional fields (like fill details) if needed.
    public OrderFilledEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
