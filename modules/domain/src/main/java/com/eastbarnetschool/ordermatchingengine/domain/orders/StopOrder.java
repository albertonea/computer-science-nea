package com.eastbarnetschool.ordermatchingengine.domain.orders;

import java.util.UUID;

public class StopOrder {
    private final Long executionPrice;
    private final UUID orderId;
    private final Order order;

    public StopOrder(Long executionPrice, Order order) {
        this.executionPrice = executionPrice;
        this.orderId = UUID.randomUUID();
        this.order = order;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Order getOrder() {
        return order;
    }

    public Long getExecutionPrice() {
        return executionPrice;
    }
}
