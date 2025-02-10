package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface BalancesRepository {
    BalanceEntity updateOrCreateBalance(UUID userId, String ticker, Long balanceDelta, Long lockedBalancesDelta);
    List<BalanceEntity> get(UUID userId);
    Boolean checkIfHasEnoughBalance(UUID userId, String ticker, Long requiredBalance);
}
