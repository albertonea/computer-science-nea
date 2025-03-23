package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;

public class PriceLevel {
    private final Long price;
    private final Queue<Order> orders;

    public PriceLevel(Long price, Order order) {
        this.price = price;
        // priority queue on the orders with the oldest taking priority
        this.orders = new Queue<>();
        orders.enqueue(order);
    }

    public Long getPrice() {
        return price;
    }

    public Queue<Order> getOrders() {
        return orders;
    }

    public void enqueue(Order order) {
        orders.enqueue(order);
    }
    
    public Order dequeue() {
        return orders.dequeue();
    }

    public Order peek() {
        return orders.peek();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }
}
