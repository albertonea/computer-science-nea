package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.LinkedList;

public class PriceLevel {
    private Long price;
    private LinkedList<Order> orders;

    public PriceLevel(Long price, Order order) {
        this.price = price;
        this.orders = new LinkedList<>();
        orders.add(order);
    }

    public Long getPrice() {
        return price;
    }

    public LinkedList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
