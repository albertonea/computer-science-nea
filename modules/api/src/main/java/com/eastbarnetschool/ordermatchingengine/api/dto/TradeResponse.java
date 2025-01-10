package com.eastbarnetschool.ordermatchingengine.api.dto;

public class TradeResponse {
    private final Long price;
    private final Integer quantity;

    public TradeResponse(Integer quantity, Long price) {
        this.quantity = quantity;
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
