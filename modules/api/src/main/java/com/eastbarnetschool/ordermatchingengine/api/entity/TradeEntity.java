package com.eastbarnetschool.ordermatchingengine.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Table("trades")
public class TradeEntity {
    @Id
    private final UUID tradeId;
    private final Timestamp tradeTime;
    private final Long price;
    private final Integer quantity;
    private final UUID buyerId;
    private final UUID sellerId;
    private final String ticker;


    public TradeEntity(UUID sellerId, String ticker, UUID buyerId, Integer quantity, Long price, Timestamp tradeTime, UUID tradeId) {
        this.sellerId = sellerId;
        this.ticker = ticker;
        this.buyerId = buyerId;
        this.quantity = quantity;
        this.price = price;
        this.tradeTime = tradeTime;
        this.tradeId = tradeId;
    }

    public UUID getTradeId() {
        return tradeId;
    }

    public Timestamp getTradeTime() {
        return tradeTime;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
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
