package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.TimeInterval;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.CandlestickDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public void insert(TradeEntity trade) {
        tradeRepository.insert(trade);
    }
    public Optional<List<CandlestickDto>> getCandlesticks(String ticker, TimeInterval timeInterval) {
        return tradeRepository.getCandlesticks(ticker, timeInterval);
    }
}
