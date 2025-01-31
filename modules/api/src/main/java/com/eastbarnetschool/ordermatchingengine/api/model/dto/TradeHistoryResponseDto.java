package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.sql.Timestamp;
import java.util.UUID;

public class TradeHistoryResponseDto {
    private UUID tradeId;
    private Long price;
    private Long quantity;
    private Timestamp tradeTime;

    public TradeHistoryResponseDto(UUID tradeId, Long price, Long quantity, Timestamp tradeTime) {
        this.tradeId = tradeId;
        this.price = price;
        this.quantity = quantity;
        this.tradeTime = tradeTime;
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

    public Timestamp getTradeTime() {
        return tradeTime;
    }
}

