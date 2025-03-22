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
with trade_buckets as (
    select
        ticker,
        date_bin(cast(:interval as interval), trade_time, '2025-01-01 00:00:00') as interval_start,
        price,
        quantity,
        trade_time
    from trades
)
select
    ticker,
    interval_start as "intervalStart",
    (array_agg(price order by trade_time asc))[1] as open,
    max(price) as high,
    min(price) as low,
    (array_agg(price order by trade_time desc))[1] as close,
    sum(quantity) as volume
from trade_buckets
where ticker = :ticker
group by interval_start, ticker
order by interval_start asc;
                    """,
                    Map.of("interval", getIntervalString(timeInterval), "ticker", ticker),
                    new BeanPropertyRowMapper<>(CandlestickDto.class)
        ));
    }
}
