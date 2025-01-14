package com.eastbarnetschool.ordermatchingengine.api.dto;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.time.Instant;
import java.util.UUID;

public class FilledOrderResponse {
    private UUID orderId;
    private Long price;
    private Integer initialQuantity;
    private String ticker;
    private Side side;
    private OrderType orderType;
    private Instant orderDate;

    public FilledOrderResponse(UUID orderId, Long price, Integer initialQuantity, String ticker, Side side, OrderType orderType, Instant orderDate) {
        this.orderId = orderId;
        this.price = price;
        this.initialQuantity = initialQuantity;
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

    public Integer getInitialQuantity() {
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
}
