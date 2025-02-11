package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.LimitOrder;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriceLevel {
    private final Long price;
    private final PriorityQueue<LimitOrder> orders;

    public PriceLevel(Long price, LimitOrder order) {
        this.price = price;
        this.orders = new PriorityQueue<>(Comparator.comparing(Order::getCreatedAt));
        orders.add(order);
    }

    public Long getPrice() {
        return price;
    }

    public PriorityQueue<LimitOrder> getOrders() {
        return orders;
    }

    public void addOrder(LimitOrder order) {
        orders.add(order);
    }
    
    public LimitOrder poll() {
        return orders.poll();
    }

    public LimitOrder peek() {
        return orders.peek();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }
}
