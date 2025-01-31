package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeHistoryResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository {
    void insert(TradeEntity trade);
    List<TradeHistoryResponseDto> getWeekHistory(String ticker);
}
