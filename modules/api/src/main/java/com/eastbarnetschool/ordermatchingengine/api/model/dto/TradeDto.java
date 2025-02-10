package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class TradeDto {
    private UUID tradeId;
    private Side side;
    private Long price;
    private Long quantity;
    private String ticker;
    private Timestamp tradeTime;

    public TradeDto() {}

    public TradeDto(UUID tradeId, Side side, Long quantity, Long price, String ticker, Timestamp tradeTime) {
        this.tradeId = tradeId;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.ticker = ticker;
        this.tradeTime = tradeTime;
    }

    public TradeDto(UUID tradeId, Long quantity, Long price, String ticker, Timestamp tradeTime) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.price = price;
        this.ticker = ticker;
        this.tradeTime = tradeTime;
    }

    public TradeDto(UUID tradeId, Long quantity, Long price, Timestamp tradeTime) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.price = price;
        this.tradeTime = tradeTime;
    }

    public String getTicker() {
        return ticker;
    }

    public Side getSide() {
        return side;
    }

    public Timestamp getTradeTime() {
        return tradeTime;
    }

    public UUID getTradeId() {
        return tradeId;
    }

    public Long getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }
}
