package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import lombok.Data;

import java.util.UUID;

public class OrderRequestDto {
    private final Long price;
    private final Long quantity;
    private final String ticker;
    private final Side side;
    private final OrderType orderType;

    public OrderRequestDto(Long price, Long quantity, String ticker, Side side, OrderType orderType) {
        this.price = price;
        this.quantity = quantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
    }

    public Long getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getTicker() {
        return ticker;
    }

    public Side getSide() {
        return side;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
