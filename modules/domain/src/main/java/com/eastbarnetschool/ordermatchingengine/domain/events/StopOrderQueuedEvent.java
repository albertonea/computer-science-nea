package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

public class StopOrderQueuedEvent {
    private final StopOrder stopOrder;

    public StopOrderQueuedEvent(StopOrder stopOrder) {
        this.stopOrder = stopOrder;
    }

    public StopOrder getStopOrder() {
        return stopOrder;
    }
}
