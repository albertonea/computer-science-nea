package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class OrderBookResponseDto {
    private final String ticker;
    private final List<OrderBookLevelDto> buySide;
    private final List<OrderBookLevelDto> sellSide;

    public OrderBookResponseDto(String ticker, List<OrderBookLevelDto> buySide, List<OrderBookLevelDto> sellSide) {
        this.ticker = ticker;
        this.buySide = buySide;
        this.sellSide = sellSide;
    }

    public String getTicker() {
        return ticker;
    }

    public List<OrderBookLevelDto> getBuySide() {
        return buySide;
    }

    public List<OrderBookLevelDto> getSellSide() {
        return sellSide;
    }
}
