package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import com.eastbarnetschool.ordermatchingengine.domain.Order;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class UserDashboardDto {
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;
    private final List<BalanceDto> balances;
    private final List<OrderDto> orders;

    public UserDashboardDto(UUID userId, String username, Timestamp createdAt, List<BalanceDto> balances, List<OrderDto> orders) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.balances = balances;
        this.orders = orders;
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
    public List<BalanceDto> getBalances() {
        return balances;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }
}
