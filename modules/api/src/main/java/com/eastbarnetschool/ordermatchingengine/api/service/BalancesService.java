package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.BalanceDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.BalanceMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.BalancesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BalancesService {
    private final BalancesRepository balancesRepository;

    public BalancesService(BalancesRepository balancesRepository) {
        this.balancesRepository = balancesRepository;
    }

    public List<BalanceEntity> getByUserId(UUID userId) {
        return balancesRepository.get(userId);
    }

    public BalanceEntity updateOrCreateBalance(UUID userId, String ticker, Long balanceDelta, Long lockedBalanceDelta) {
        return balancesRepository.updateOrCreateBalance(userId, ticker, balanceDelta, lockedBalanceDelta);
    }

    public Boolean checkIfHasEnoughBalance(UUID userId, String ticker, Long requiredBalance) {
        return balancesRepository.checkIfHasEnoughBalance(userId, ticker, requiredBalance);
    }
}
