package com.eastbarnetschool.ordermatchingengine.api.model.dto;

public class OrderBookLevelDto {
    private Long price;
    private Long totalQuantity;

    public OrderBookLevelDto(Long price, Long totalQuantity) {
        this.price = price;
        this.totalQuantity = totalQuantity;
    }

    public Long getPrice() { return price; }
    public Long getTotalQuantity() { return totalQuantity; }
}
