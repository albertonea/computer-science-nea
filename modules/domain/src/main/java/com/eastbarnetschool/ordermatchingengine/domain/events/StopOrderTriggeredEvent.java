package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

public class StopOrderTriggeredEvent {
    private final StopOrder stopOrder;
    public StopOrderTriggeredEvent(StopOrder stopOrder) {
        this.stopOrder = stopOrder;
    }

    public StopOrder getStopOrder() {
        return stopOrder;
    }
}
