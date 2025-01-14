package com.eastbarnetschool.ordermatchingengine.api.entity;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Table("open_orders")
public class OrderEntity {
    @Id
    private final UUID orderId;
    private final UUID userId;
    private final Side side;
    private final Integer initialQuantity;
    private final Integer remainingQuantity;
    private final String ticker;
    private final Long price;
    private final Timestamp createdAt;

    public OrderEntity(Timestamp createdAt, Long price, String ticker, Integer remainingQuantity, Integer initialQuantity, UUID userId, UUID orderId, Side side) {
        this.createdAt = createdAt;
        this.price = price;
        this.ticker = ticker;
        this.remainingQuantity = remainingQuantity;
        this.initialQuantity = initialQuantity;
        this.side = side;
        this.userId = userId;
        this.orderId = orderId;
    }

    public Side getSide() {
        return side;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTicker() {
        return ticker;
    }

    public Long getPrice() {
        return price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
