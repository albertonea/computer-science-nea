package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.HashMap;
import java.util.Hashtable;

import static com.eastbarnetschool.ordermatchingengine.domain.OrderType.LIMIT;
public class OrderGateway {
    private HashMap<String, MatchingEngine> matchingEngines;

    public OrderGateway() {
        matchingEngines = new HashMap<>();
    }

    public void placeOrder(Order order) {
        MatchingEngine matchingEngine = matchingEngines.get(order.getTicker());
        if (matchingEngine == null) {
            MatchingEngine newMatchingEngine = new MatchingEngine(order.getTicker());
//            newMatchingEngine.start(order.getTicker());
            matchingEngines.put(order.getTicker(), newMatchingEngine);
            newMatchingEngine.placeOrder(order);
        } else {
            matchingEngine.placeOrder(order);
        }
    }

    public HashMap<String, MatchingEngine> getMatchingEngines() {
        return new HashMap<>(matchingEngines);
    }
}
