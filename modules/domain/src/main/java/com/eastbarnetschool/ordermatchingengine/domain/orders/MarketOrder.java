package com.eastbarnetschool.ordermatchingengine.domain.orders;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.time.Instant;
import java.util.UUID;

public class MarketOrder extends Order {
    private Long executedValue;

    public MarketOrder(Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        super(initialQuantity, remainingQuantity, ticker, side, orderType, userId, createdAt);
        this.executedValue = 0L;
    }

    public MarketOrder(UUID orderId, Long initialQuantity, Long remainingQuantity, String ticker, Side side, OrderType orderType, UUID userId, Instant createdAt) {
        super(orderId, initialQuantity, remainingQuantity, ticker, side, orderType, userId, createdAt);
        this.executedValue = 0L;
    }

    public Long getTotalBoughtSold() {
        return executedValue;
    }

    public void increaseTotalValue(Long value) {
        this.executedValue = executedValue + value;
    }

    public boolean isFilled(Long price) {
        return getRemainingQuantity() >= price;
    }
}
