package com.eastbarnetschool.ordermatchingengine.domain.orders;

import java.util.UUID;

public class StopOrder {
    private final UUID orderId;
    private final Long executionPrice;
    private final Order order;

    public StopOrder(Order order, Long executionPrice) {
        this.orderId = UUID.randomUUID();
        this.order = order;
        this.executionPrice = executionPrice;
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
