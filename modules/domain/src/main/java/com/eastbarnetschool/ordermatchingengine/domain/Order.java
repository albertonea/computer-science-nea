package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;

public class Order {
    private Long price;
    private String userId;
    private Integer initialQuantity;
    private Integer remainingQuantity;
    private String ticker;
    private Side side;
    private OrderType orderType;
    private Instant createdAt;

    public Order(Long price, Integer quantity, String ticker, Side side, OrderType orderType, String userId, Instant createdAt) {
        this.price = price;
        this.initialQuantity = quantity;
        this.remainingQuantity = quantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public Long getPrice() {
        return price;
    }

    public String getTicker() {
        return ticker;
    }

    public Side getSide() {
        return side;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public Integer fill(Integer fillQuantity) {
        if (fillQuantity > remainingQuantity) {
            throw new IllegalArgumentException("Fill quantity exceeds remaining quantity");
        }
        remainingQuantity -= fillQuantity;

        return remainingQuantity;
    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
