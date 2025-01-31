package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.Side;

public class OrderBookEntryDto {
    private final String ticker;
    private final Long price;
    private final Side side;
    private final Long totalQuantity;

    public OrderBookEntryDto(String ticker, Long price, Side side, Long totalQuantity) {
        this.ticker = ticker;
        this.price = price;
        this.side = side;
        this.totalQuantity = totalQuantity;
    }

    public String getTicker() {
        return ticker;
    }

    public Long getPrice() {
        return price;
    }

    public Side getSide() {
        return side;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }
}
