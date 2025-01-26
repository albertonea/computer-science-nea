package com.eastbarnetschool.ordermatchingengine.api.model.dto;

public class BalanceDto {
    private String ticker;
    private Long balance;
    private Long lockedBalance;

    public BalanceDto() {}

    public BalanceDto(String ticker, Long balance, Long lockedBalance) {
        this.ticker = ticker;
        this.balance = balance;
        this.lockedBalance = lockedBalance;
    }

    public void setLockedBalance(Long lockedBalance) {
        this.lockedBalance = lockedBalance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
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
