package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

import java.time.Instant;
import java.util.UUID;

public class Trade {
    private final UUID tradeId;
    private final Instant tradeTime;
    private final Long price;
    private final Long quantity;
    private final Order buyOrder;
    private final Order sellOrder;
    private final String ticker;

    public Trade(Instant tradeTime, Long price, Long quantity, Order buyOrder, Order sellOrder, String ticker) {
        this.tradeId = UUID.randomUUID();
        this.tradeTime = tradeTime;
        this.price = price;
        this.quantity = quantity;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.ticker = ticker;
    }

    public UUID getTradeId() {
        return tradeId;
    }

    public Instant getTradeTime() {
        return tradeTime;
    }

    public Long getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }
    public Order getSellOrder() {
        return sellOrder;
    }

    public String getTicker() {
        return ticker;
    }
}
