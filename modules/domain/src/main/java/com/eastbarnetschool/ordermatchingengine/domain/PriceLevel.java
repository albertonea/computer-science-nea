package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.LinkedList;

public class PriceLevel {
    private Long price;
    private LinkedList<Order> orders;


    public Long getPrice() {
        return price;
    }

    public LinkedList<Order> getOrders() {
        return orders;
    }
}
