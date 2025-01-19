package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.BalanceEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface BalancesRepository {
    BalanceEntity updateOrCreateBalance(UUID userId, String ticker, Integer balanceDelta, Integer lockedBalancesDelta);
    ArrayList<BalanceEntity> get(UUID userId);
    Boolean checkIfHasEnoughBalance(UUID userId, String ticker, Integer requiredBalance);
}
