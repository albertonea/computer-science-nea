package com.eastbarnetschool.ordermatchingengine.domain.listeners;

import com.eastbarnetschool.ordermatchingengine.domain.events.OrderEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.TradeEvent;

public interface TradingEventListener {
    void onTrade(TradeEvent event);
    void onOrderEvent(OrderEvent event);
}
