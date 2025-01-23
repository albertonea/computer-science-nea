package com.eastbarnetschool.ordermatchingengine.api.dto;

import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {
    private Long price;
    private UUID userId;
    private Long quantity;
    private String ticker;
    private Side side;
    private OrderType orderType;
}
