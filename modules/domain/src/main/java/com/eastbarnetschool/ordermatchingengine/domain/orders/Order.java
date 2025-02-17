package com.eastbarnetschool.ordermatchingengine.domain.orders;
import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.time.Instant;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final UUID userId;
    private final Long initialQuantity;
    private Long remainingQuantity;
    private Long executedValue;
    private Long price;
    private final String ticker;
    private final Side side;
    private final OrderType orderType;
    private final Instant createdAt;

    public Order(UUID orderId, Long initialQuantity, Long remainingQuantity, Long price, Long executedValue, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        this.orderId = orderId;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.price = price;
        this.executedValue = executedValue;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Order(Long initialQuantity, Long remainingQuantity, Long price, Long executedValue, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        this.orderId = UUID.randomUUID();
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.price = price;
        this.executedValue = executedValue;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Order(Long initialQuantity, Long remainingQuantity, Long executedValue, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        this.orderId = UUID.randomUUID();
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.executedValue = executedValue;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Order(UUID orderId, Long initialQuantity, Long remainingQuantity, Long executedValue, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        this.orderId = orderId;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.executedValue = executedValue;
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

    public Long getExecutedValue() {
        return executedValue;
    }

    public Long getPrice() {
        return price;
    }

    public void fill(Long fillQuantity, Long fillPrice) {
        if (fillQuantity > remainingQuantity) {
            throw new IllegalArgumentException("Fill quantity exceeds remaining quantity");
        }
        remainingQuantity -= fillQuantity;
        executedValue += fillPrice * fillQuantity;
    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    public boolean isStopOrder() {
        return orderType == OrderType.STOPLIMIT ||
                orderType == OrderType.STOPMARKET;
    }

    public boolean isMarketOrder() {
        return orderType == OrderType.MARKET ||
                orderType == OrderType.STOPMARKET;
    }

    public boolean isLimitOrder() {
        return orderType == OrderType.LIMIT ||
                orderType == OrderType.STOPLIMIT;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
