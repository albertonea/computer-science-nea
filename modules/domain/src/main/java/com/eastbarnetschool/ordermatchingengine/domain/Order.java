package com.eastbarnetschool.ordermatchingengine.domain;
import java.time.Instant;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final Long price;
    private final UUID userId;
    private final Long initialQuantity;
    private Long remainingQuantity;
    private final String ticker;
    private final Side side;
    private final OrderType orderType;
    private final Instant createdAt;

    public Order(Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        this.orderId = UUID.randomUUID();
        this.price = price;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Order(UUID orderId, Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        this.orderId = orderId;
        this.price = price;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
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

    public Long getInitialQuantity() {
        return initialQuantity;
    }

    public Long getRemainingQuantity() {
        return remainingQuantity;
    }

    public void fill(Long fillQuantity) {
        if (fillQuantity > remainingQuantity) {
            throw new IllegalArgumentException("Fill quantity exceeds remaining quantity");
        }
        remainingQuantity -= fillQuantity;

    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
