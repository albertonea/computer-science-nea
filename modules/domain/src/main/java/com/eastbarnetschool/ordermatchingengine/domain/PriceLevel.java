package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.LinkedList;

public class PriceLevel {
    private Double price;
    private LinkedList<Order> orders;

    public PriceLevel(Double price, Order order) {
        this.price = price;
        this.orders = new LinkedList<>();
        orders.add(order);
    }

    public Double getPrice() {
        return price;
    }

    public LinkedList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
