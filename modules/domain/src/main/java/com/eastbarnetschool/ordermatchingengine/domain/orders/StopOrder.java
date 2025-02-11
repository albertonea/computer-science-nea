package com.eastbarnetschool.ordermatchingengine.domain.orders;

public class StopOrder {
    private final Long executionPrice;
    private final Order order;

    public StopOrder(Order order, Long executionPrice) {
        this.order = order;
        this.executionPrice = executionPrice;
    }

    public Order getOrder() {
        return order;
    }

    public Long getExecutionPrice() {
        return executionPrice;
    }
}
