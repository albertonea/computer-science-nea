package com.eastbarnetschool.ordermatchingengine.api.dto;

public class TradeResponse {
    private final Long price;
    private final Long quantity;

    public TradeResponse(Long quantity, Long price) {
        this.quantity = quantity;
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }
}
