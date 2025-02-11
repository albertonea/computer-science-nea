package com.eastbarnetschool.ordermatchingengine.domain.orders;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.time.Instant;
import java.util.UUID;

public class OrderFactory {
    public Order createMarketOrder(Long quantity, String ticker, Side side, UUID userId) {
        return new MarketOrder(quantity, quantity, ticker, side, OrderType.MARKET, userId, Instant.now());
    }

    public Order createLimitOrder(Long price, Long quantity, String ticker, Side side, UUID userId) {
        return new LimitOrder(price, quantity, quantity, ticker, side, OrderType.LIMIT, userId, Instant.now());
    }
}
