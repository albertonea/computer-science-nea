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
    private Timestamp orderDate;

    public OrderDto() {}

    public OrderDto(UUID orderId, Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, Timestamp orderDate) {
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

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public Long getRemainingQuantity() {
        return remainingQuantity;
    }
}

