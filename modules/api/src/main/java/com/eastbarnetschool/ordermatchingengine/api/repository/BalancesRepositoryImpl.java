package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.Balance;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Repository
public class BalancesRepositoryImpl implements BalancesRepository{

    private final NamedParameterJdbcTemplate template;

    public BalancesRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Balance updateOrCreateBalance(UUID userId, String ticker, Long balanceDelta, Long lockedBalanceDelta) {
        return template.queryForObject(
                        "insert into balances (user_id, ticker, balance, locked_balance) " +
                        "values (:userId, :ticker, :balance, :lockedBalance) " +
                        "on conflict(user_id, ticker) " +
                        "do update set balance = balances.balance + excluded.balance, locked_balance = balances.locked_balance + excluded.locked_balance " +
                        "returning user_id, ticker, balance, locked_balance",
                Map.of("userId", userId, "ticker", ticker, "balance", 100000, "lockedBalance", 0),
                (rs, rowId) -> new Balance(rs.getObject("user_id", UUID.class), rs.getObject("ticker", String.class), rs.getObject("balance", Long.class), rs.getObject("locked_balance", Long.class))
        );
    }

    @Override
    public ArrayList<Balance> get(UUID userId) {
        return (ArrayList<Balance>) template.query(
                "select * from balances where user_id = :userId",
                Map.of("userId", userId),
                (rs, rowId) -> new Balance(rs.getObject("user_id", UUID.class), rs.getObject("ticker", String.class), rs.getObject("balance", Long.class), rs.getObject("locked_balance", Long.class))
        );
    }
}
