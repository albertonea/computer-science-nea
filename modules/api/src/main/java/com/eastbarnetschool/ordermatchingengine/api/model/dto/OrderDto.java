package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class OrderDto {
    private UUID orderId;
    private Long price;
    private Long initialQuantity;
    private Long remainingQuantity;
    private String ticker;
    private Side side;
    private OrderType orderType;
    private Timestamp createdAt;
    private Long executedValue;

    public OrderDto() {}

    public OrderDto(UUID orderId, Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, Timestamp createdAt, Long executedValue) {
        this.orderId = orderId;
        this.price = price;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.createdAt = createdAt;
        this.executedValue = executedValue;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Long getPrice() {
        return price;
    }

    public Long getInitialQuantity() {
        return initialQuantity;
    }

    public String getTicker() {
        return ticker;
    }

    public Side getSide() {
        return side;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Long getRemainingQuantity() {
        return remainingQuantity;
    }

    public Long getExecutedValue() {
        return executedValue;
    }
}

