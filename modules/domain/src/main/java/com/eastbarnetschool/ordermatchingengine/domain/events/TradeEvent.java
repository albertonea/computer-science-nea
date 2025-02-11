package com.eastbarnetschool.ordermatchingengine.domain.events;

import com.eastbarnetschool.ordermatchingengine.domain.Trade;

public class TradeEvent {
    private final Trade trade;
    public TradeEvent(Trade trade) {
        this.trade = trade;
    }
    public Trade getTrade() {
        return trade;
    }
}


