package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.time.Instant;
import java.util.UUID;

public class OrderDto {
    private final UUID orderId;
    private final Long price;
    private final Long initialQuantity;
    private final Long remainingQuantity;
    private final String ticker;
    private final Side side;
    private final OrderType orderType;
    private final Instant orderDate;

    public OrderDto(UUID orderId, Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, Instant orderDate) {
        this.orderId = orderId;
        this.price = price;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.orderDate = orderDate;
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

    public Instant getOrderDate() {
        return orderDate;
    }

    public Long getRemainingQuantity() {
        return remainingQuantity;
    }
}

