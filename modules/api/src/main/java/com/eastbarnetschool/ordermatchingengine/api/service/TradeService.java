package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeHistoryResponseDto;
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
    public List<TradeHistoryResponseDto> getTradeHistory(String ticker) {
        return tradeRepository.getWeekHistory(ticker);
    }
}
