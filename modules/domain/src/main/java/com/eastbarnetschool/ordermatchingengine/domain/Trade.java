package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;

public class Trade {
    private Instant timestamp;
    private Long price;
    private Integer quantity;
    public Trade(Instant timestamp, Long price, Integer quantity) {
        this.timestamp = timestamp;
        this.price = price;
        this.quantity = quantity;
    }
}
