package com.eastbarnetschool.ordermatchingengine.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class OrderGateway {
    private HashMap<String, MatchingEngine> matchingEngines;

    public OrderGateway() {
        matchingEngines = new HashMap<>();
    }

    public ArrayList<Trade> placeOrder(Order order) {
        MatchingEngine matchingEngine = matchingEngines.get(order.getTicker());
        if (matchingEngine == null) {
            MatchingEngine newMatchingEngine = new MatchingEngine(order.getTicker());
//            newMatchingEngine.start(order.getTicker());
            matchingEngines.put(order.getTicker(), newMatchingEngine);
            return newMatchingEngine.placeOrder(order);
        } else {
            return matchingEngine.placeOrder(order);
        }
    }

    public HashMap<String, MatchingEngine> getMatchingEngines() {
        return new HashMap<>(matchingEngines);
    }
}
