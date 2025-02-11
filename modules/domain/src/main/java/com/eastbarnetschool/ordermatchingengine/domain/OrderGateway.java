package com.eastbarnetschool.ordermatchingengine.domain;

import com.eastbarnetschool.ordermatchingengine.domain.events.OrderEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.TradeEvent;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.TradingEventListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderGateway {
    private final Map<String, OrderQueue> orderQueues;
    private final List<TradingEventListener> listeners;

    public OrderGateway() {
        orderQueues = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public void addTradingEventListener(TradingEventListener listener) {
        listeners.add(listener);
    }

    public void removeTradingEventListener(TradingEventListener listener) {
        listeners.remove(listener);
    }

    public void publishTradeEvent(TradeEvent event) {
        for (TradingEventListener listener : listeners) {
            listener.onTrade(event);
        }
    }

    public void publishOrderEvent(OrderEvent event) {
        for (TradingEventListener listener : listeners) {
            listener.onOrderEvent(event);
        }
    }

    public OrderQueue getOrderQueue(String ticker) {
        OrderQueue orderQueue = orderQueues.get(ticker);
        if (orderQueue == null) {
            orderQueue = new OrderQueue(ticker, this);
            orderQueues.put(ticker, orderQueue);
        }
        return orderQueue;
    }

    public void placeOrder(Order order) {
        getOrderQueue(order.getTicker()).placeOrder(order);
    }

    public void placeStopOrder(StopOrder order) {
        getOrderQueue(order.getOrder().getTicker()).placeStopOrder(order);
    }
}
