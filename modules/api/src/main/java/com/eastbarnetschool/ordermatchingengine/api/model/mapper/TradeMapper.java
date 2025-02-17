package com.eastbarnetschool.ordermatchingengine.api.model.mapper;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.domain.Trade;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TradeMapper {
    public TradeDto toTradeDto(Trade trade) {
        return new TradeDto(
                trade.getTradeId(),
                trade.getQuantity(),
                trade.getPrice(),
                Timestamp.from(trade.getTradeTime())
        );
    }

    public TradeDto toTradeDto(TradeEntity tradeEntity) {
        return new TradeDto(
            tradeEntity.getTradeId(),
            tradeEntity.getQuantity(),
            tradeEntity.getPrice(),
            tradeEntity.getTicker(),
            tradeEntity.getTradeTime()
        );
    }

    public TradeEntity toTradeEntity(Trade trade) {
        return new TradeEntity(
                trade.getSellOrder().getUserId(),
                trade.getTicker(),
                trade.getBuyOrder().getUserId(),
                trade.getQuantity(),
                trade.getPrice(),
                Timestamp.from(trade.getTradeTime()),
                trade.getTradeId()
        );
    }
}
