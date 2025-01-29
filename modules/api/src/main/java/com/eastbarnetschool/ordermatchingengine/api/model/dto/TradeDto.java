package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import java.sql.Timestamp;
import java.time.Instant;

public class TradeDto {
    private String tradeId;
    private boolean buy;
    private Long price;
    private Long quantity;
    private String ticker;
    private Timestamp tradeTime;

    public TradeDto() {}

    public TradeDto(String tradeId, boolean buy, Long quantity, Long price, String ticker, Timestamp tradeTime) {
        this.tradeId = tradeId;
        this.buy = buy;
        this.quantity = quantity;
        this.price = price;
        this.ticker = ticker;
        this.tradeTime = tradeTime;
    }

    public String getTicker() {
        return ticker;
    }

    public boolean isBuy() {
        return buy;
    }

    public Timestamp getTradeTime() {
        return tradeTime;
    }

    public String getTradeId() {
        return tradeId;
    }

    public Long getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }
}
