package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import lombok.Data;

@Data
public class OrderRequest {
    private Long price;
    private String username;
    private Integer quantity;
    private String ticker;
    private Side side;
    private OrderType orderType;
}
