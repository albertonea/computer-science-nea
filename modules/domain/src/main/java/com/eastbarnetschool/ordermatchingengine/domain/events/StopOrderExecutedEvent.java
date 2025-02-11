package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

public class StopOrderExecutedEvent {
    private final StopOrder stopOrder;
    public StopOrderExecutedEvent(StopOrder stopOrder) {
        this.stopOrder = stopOrder;
    }

    public StopOrder getStopOrder() {
        return stopOrder;
    }
}
