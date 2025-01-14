package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public void insert(TradeEntity trade) {
        tradeRepository.insert(trade);
    }
}
