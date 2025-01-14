package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.TradeEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TradeRepositoryImpl implements TradeRepository {
    private final NamedParameterJdbcTemplate template;

    public TradeRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void insert(TradeEntity trade) {
        template.update("insert into trades (trade_id, buyer_id, ticker, price, quantity, seller_id, trade_time) values (:tradeId, :buyerId, :ticker, :price, :quantity, :sellerId, :tradeTime)",
                Map.of("tradeId", trade.getTradeId(), "buyerId", trade.getBuyerId(), "ticker", trade.getTicker(), "price", trade.getPrice(), "quantity", trade.getQuantity(), "sellerId", trade.getSellerId(), "tradeTime", trade.getTradeTime())
        );
    }
}
