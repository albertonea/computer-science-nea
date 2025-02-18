package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.BalancesRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BalancesRepositoryImpl implements BalancesRepository {

    private final NamedParameterJdbcTemplate template;

    public BalancesRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public BalanceEntity updateOrCreateBalance(UUID userId, String ticker, Long balanceDelta, Long lockedBalanceDelta) {
        return template.queryForObject(
                """
                    insert into balances (user_id, ticker, balance, locked_balance)
                    values (:userId, :ticker, :balance, :lockedBalance)
                    on conflict(user_id, ticker)
                    do update
                        set balance = balances.balance + excluded.balance,
                        locked_balance = balances.locked_balance + excluded.locked_balance
                    returning user_id, ticker, balance, locked_balance
                    """,
                Map.of("userId", userId, "ticker", ticker, "balance", balanceDelta, "lockedBalance", lockedBalanceDelta),
                (rs, rowId) -> new BalanceEntity(rs.getObject("user_id", UUID.class), rs.getObject("ticker", String.class), rs.getObject("balance", Long.class), rs.getObject("locked_balance", Long.class))
        );
    }

    @Override
    public List<BalanceEntity> get(UUID userId) {
        return template.query(
                """
                    select *
                    from balances
                    where user_id = :userId
                    """,
                Map.of("userId", userId),
                (rs, rowId) -> new BalanceEntity(rs.getObject("user_id", UUID.class), rs.getObject("ticker", String.class), rs.getObject("balance", Long.class), rs.getObject("locked_balance", Long.class))
        );
    }

    @Override
    public Boolean checkIfHasEnoughBalance(UUID userId, String ticker, Long requiredBalance) {
        Optional<Long> balance = Optional.ofNullable(template.queryForObject(
            """
                select balance
                from balances
                where
                    user_id = :userId
                    and
                    ticker = :ticker
                """,
                Map.of("userId", userId, "ticker", ticker),
                Long.class
        ));

        return balance.isPresent();
    }
}
