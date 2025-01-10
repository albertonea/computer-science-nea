package com.eastbarnetschool.ordermatchingengine.domain;

import java.time.Instant;
import java.util.ArrayList;

public class Trade {
    private Instant timestamp;
    private Long price;
    private Integer quantity;
    private String buyerId;
    private String sellerId;
    private String ticker;
    private ArrayList<Order> orders;

    public Trade(Instant timestamp, Long price, Integer quantity, ArrayList<Order> orders) {
        this.timestamp = timestamp;
        this.price = price;
        this.quantity = quantity;
        this.orders = orders;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getTicker() {
        return ticker;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
