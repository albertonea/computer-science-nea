package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PriceLevel {
    private final Long price;
    private final PriorityQueue<Order> orders;

    public PriceLevel(Long price, Order order) {
        this.price = price;
        this.orders = new PriorityQueue<>(Comparator.comparing(Order::getCreatedAt));
        orders.add(order);
    }

    public Long getPrice() {
        return price;
    }

    public PriorityQueue<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
    
    public Order poll() {
        return orders.poll();
    }

    public Order peek() {
        return orders.peek();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }
}
