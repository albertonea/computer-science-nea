package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.TimeInterval;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.CandlestickDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeHistoryResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository {
    void insert(TradeEntity trade);
    Optional<List<CandlestickDto>> getCandlesticks(String ticker, TimeInterval timeInterval);
}
