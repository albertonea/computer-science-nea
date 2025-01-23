package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.TradeRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TradeRepositoryImpl implements TradeRepository {
    private final NamedParameterJdbcTemplate template;

    public TradeRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void insert(TradeEntity trade) {
        template.update(
                "insert into trades (trade_id, buyer_id, ticker, price, quantity, seller_id, trade_time)" +
                    " values (:tradeId, :buyerId, :ticker, :price, :quantity, :sellerId, :tradeTime)",
                trade.toQueryParameters()
        );
    }
}
