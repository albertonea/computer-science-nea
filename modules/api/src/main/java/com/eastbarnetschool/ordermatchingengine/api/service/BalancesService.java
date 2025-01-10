package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.entity.Balance;
import com.eastbarnetschool.ordermatchingengine.api.repository.BalancesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class BalancesService {
    private final BalancesRepository balancesRepository;

    public BalancesService(BalancesRepository balancesRepository) {
        this.balancesRepository = balancesRepository;
    }

    public ArrayList<Balance> get(UUID userId) {
        return balancesRepository.get(userId);
    }

    public Balance updateOrCreateBalance(UUID userId, String ticker, Long balanceDelta, Long lockedBalancesDelta) {
        return balancesRepository.updateOrCreateBalance(userId, ticker, balanceDelta, lockedBalancesDelta);
    }
}
