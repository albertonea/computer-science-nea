package com.eastbarnetschool.ordermatchingengine.api.dto;

import com.eastbarnetschool.ordermatchingengine.api.entity.BalanceEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class UserWithBalancesResponse {
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;
    private final ArrayList<BalanceEntity> balances;

    public UserWithBalancesResponse(UUID userId, String username, Timestamp createdAt, ArrayList<BalanceEntity> balances) {
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
    public ArrayList<BalanceEntity> getBalances() {
        return balances;
    }
}
