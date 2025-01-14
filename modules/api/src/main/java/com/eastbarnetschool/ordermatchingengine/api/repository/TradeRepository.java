package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.TradeEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TradeRepository {
    void insert(TradeEntity trade);
}
