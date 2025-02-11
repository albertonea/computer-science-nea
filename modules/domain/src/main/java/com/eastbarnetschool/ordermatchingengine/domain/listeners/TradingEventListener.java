package com.eastbarnetschool.ordermatchingengine.domain.listeners;

import com.eastbarnetschool.ordermatchingengine.domain.events.*;

public interface TradingEventListener {
    void onTrade(TradeEvent event);
    void onOrderFilledEvent(OrderFilledEvent event);
    void onOrderPlacedEvent(OrderPlacedEvent event);
    void onStopOrderQueuedEvent(StopOrderQueuedEvent event);
    void onStopOrderExecutedEvent(StopOrderExecutedEvent event);
}
