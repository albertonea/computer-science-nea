package com.eastbarnetschool.ordermatchingengine.api.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("stop_orders")
public class StopOrderEntity {
    @Id
    private UUID id;
    private UUID orderId;
    private Long executionPrice;

    public StopOrderEntity(UUID id, UUID orderId, Long executionPrice) {
        this.id = id;
        this.orderId = orderId;
        this.executionPrice = executionPrice;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Long getExecutionPrice() {
        return executionPrice;
    }
}
