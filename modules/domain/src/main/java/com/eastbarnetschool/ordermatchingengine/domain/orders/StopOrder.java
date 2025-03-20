package com.eastbarnetschool.ordermatchingengine.domain.orders;

import java.util.UUID;

public class StopOrder {
    private final Long triggerPrice;
    private final UUID orderId;
    private final Order order;

    public StopOrder(Long triggerPrice, Order order) {
        this.triggerPrice = triggerPrice;
        this.orderId = UUID.randomUUID();
        this.order = order;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Order getOrder() {
        return order;
    }

    public Long getTriggerPrice() {
        return triggerPrice;
    }
}
