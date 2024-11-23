package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.PriorityQueue;

public class OrderBook {
    private PriorityQueue<PriceLevel> sellSide;
    private PriorityQueue<PriceLevel> buySide;

    public OrderBook() {
        sellSide = new PriorityQueue<>();
        buySide = new PriorityQueue<>();
    }

    public PriorityQueue<PriceLevel> getBuySide() {
        return buySide;
    }

    public PriorityQueue<PriceLevel> getSellSide() {
        return sellSide;
    }
}
