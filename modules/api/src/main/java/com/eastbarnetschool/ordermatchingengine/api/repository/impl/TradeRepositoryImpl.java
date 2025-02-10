package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeHistoryResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.TradeRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public List<TradeHistoryResponseDto> getWeekHistory(String ticker) {
        return template.query(
                """
                select * from trades where ticker = :ticker and trade_time > now() - interval '7 days'
                """,
                Map.of("ticker", ticker),
                (rs, rowNum) -> new TradeHistoryResponseDto(
                        rs.getObject("trade_id", UUID.class),
                        rs.getObject("price", Long.class),
                        rs.getObject("quantity", Long.class),
                        rs.getTimestamp("trade_time")
                )
        );
    }
}
