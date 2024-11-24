package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;

public class Trade {
    private Instant timestamp;
    private Double price;
    private Integer quantity;
    public Trade(Instant timestamp, Double price, Integer quantity) {
        this.timestamp = timestamp;
        this.price = price;
        this.quantity = quantity;
    }
}
