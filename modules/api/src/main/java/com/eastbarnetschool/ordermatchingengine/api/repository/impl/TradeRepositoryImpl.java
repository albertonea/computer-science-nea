package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.TimeInterval;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.CandlestickDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeHistoryResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.TradeRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private String getIntervalString(TimeInterval interval) {
        return switch (interval) {
            case FIVE_MINUTES -> "5 minutes";
            case FIFTEEN_MINUTES -> "15 minutes";
            case ONE_HOUR -> "1 hour";
            case FOUR_HOURS -> "4 hours";
        };
    }

    @Override
    public Optional<List<CandlestickDto>> getCandlesticks(String ticker, TimeInterval timeInterval) {
        return Optional.of(template.query(
                """
                    WITH binned_trades AS (
                        SELECT
                            ticker,
                            date_bin(CAST(:interval AS interval), trade_time, '2001-01-01 00:00:00') AS interval_start,
                            price,
                            quantity,
                            trade_time
                        FROM trades
                    )
                    SELECT
                        ticker,
                        interval_start as "intervalStart",
                        (array_agg(price ORDER BY trade_time ASC))[1] AS open,
                        MAX(price) AS high,
                        MIN(price) AS low,
                        (array_agg(price ORDER BY trade_time DESC))[1] AS close,
                        SUM(quantity) AS volume
                    FROM binned_trades
                    WHERE ticker = :ticker
                    GROUP BY interval_start, ticker
                    ORDER BY interval_start ASC;
                    """,
                    Map.of("interval", getIntervalString(timeInterval), "ticker", ticker),
                    new BeanPropertyRowMapper<>(CandlestickDto.class)
        ));
    }
}
