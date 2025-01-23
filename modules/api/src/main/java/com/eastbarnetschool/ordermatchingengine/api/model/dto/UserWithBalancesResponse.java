package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserWithBalancesResponse {
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;
    private final List<Balance> balances;

    public UserWithBalancesResponse(UUID userId, String username, Timestamp createdAt, List<Balance> balances) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.balances = balances;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public List<Balance> getBalances() {
        return balances;
    }
}
