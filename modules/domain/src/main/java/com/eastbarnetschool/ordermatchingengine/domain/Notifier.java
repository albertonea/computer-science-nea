package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.events.*;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.TradingEventListener;

public class Notifier implements TradingEventListener {
    @Override
    public void onTrade(TradeEvent event) {
        System.out.println("Trade");
    }

    @Override
    public void onOrderFilledEvent(OrderFilledEvent event) {
        System.out.println("Filled an order");
    }

    @Override
    public void onOrderPlacedEvent(OrderPlacedEvent event) {
        System.out.println("Placed an order");
    }

    @Override
    public void onStopOrderQueuedEvent(StopOrderQueuedEvent event) {
        System.out.println("Stop order queued");
    }

    @Override
    public void onStopOrderExecutedEvent(StopOrderExecutedEvent event) {
        System.out.println("Stop order executed");
    }
}
