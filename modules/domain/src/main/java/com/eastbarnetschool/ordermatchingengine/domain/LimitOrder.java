package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;
import java.util.UUID;

public class LimitOrder extends Order {
    private final Long price;

    public LimitOrder(Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        super(initialQuantity, remainingQuantity, ticker, side, orderType, userId, createdAt);
        this.price = price;
    }

    public LimitOrder(UUID orderID, Long price, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        super(orderID, initialQuantity, remainingQuantity, ticker, side, orderType, userId, createdAt);
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }
}
