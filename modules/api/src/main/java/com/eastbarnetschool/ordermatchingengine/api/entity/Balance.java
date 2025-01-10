package com.eastbarnetschool.ordermatchingengine.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("balances")
public class Balance {
    @Id
    private final UUID userId;
    private final String ticker;

    private final Long balance;
    private final Long lockedBalance;

    public Balance(UUID userId, String ticker, Long balance, Long lockedBalance) {
        this.userId = userId;
        this.ticker = ticker;
        this.balance = balance;
        this.lockedBalance = lockedBalance;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTicker() {
        return ticker;
    }

    public Long getBalance() {
        return balance;
    }

    public Long getLockedBalance() {
        return lockedBalance;
    }
}
