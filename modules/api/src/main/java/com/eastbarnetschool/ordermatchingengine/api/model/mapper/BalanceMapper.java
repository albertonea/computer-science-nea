package com.eastbarnetschool.ordermatchingengine.api.model.mapper;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.BalanceDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {
    public BalanceDto toBalanceDto(BalanceEntity balanceEntity) {
        return new BalanceDto(
                balanceEntity.getTicker(),
                balanceEntity.getBalance(),
                balanceEntity.getLockedBalance()
        );
    }
}
