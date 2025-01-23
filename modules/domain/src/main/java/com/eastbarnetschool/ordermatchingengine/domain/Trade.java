package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;
import java.util.UUID;

public class Trade {
    private final UUID tradeId;
    private final Instant tradeTime;
    private final Long price;
    private final Long quantity;
    private final UUID buyerId;
    private final UUID sellerId;
    private final String ticker;

    public Trade(Instant tradeTime, Long price, Long quantity, UUID buyerId, UUID sellerId, String ticker) {
        this.tradeId = UUID.randomUUID();
        this.tradeTime = tradeTime;
        this.price = price;
        this.quantity = quantity;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
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

    public UUID getBuyerId() {
        return buyerId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public String getTicker() {
        return ticker;
    }
}
