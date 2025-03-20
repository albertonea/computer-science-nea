package com.eastbarnetschool.ordermatchingengine.api.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("stop_orders")
public class StopOrderEntity {
    @Id
    private UUID id;
    private UUID orderId;
    private Long triggerPrice;

    public StopOrderEntity(UUID id, UUID orderId, Long triggerPrice) {
        this.id = id;
        this.orderId = orderId;
        this.triggerPrice = triggerPrice;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Long getTriggerPrice() {
        return triggerPrice;
    }
}
