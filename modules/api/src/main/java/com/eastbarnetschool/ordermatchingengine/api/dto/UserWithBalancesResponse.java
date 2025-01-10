package com.eastbarnetschool.ordermatchingengine.api.dto;

import com.eastbarnetschool.ordermatchingengine.api.entity.Balance;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class UserWithBalancesResponse {
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;
    private final ArrayList<Balance> balances;

    public UserWithBalancesResponse(UUID userId, String username, Timestamp createdAt, ArrayList<Balance> balances) {
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
    public ArrayList<Balance> getBalances() {
        return balances;
    }
}
