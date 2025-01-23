package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository {
    void insert(TradeEntity trade);
}
