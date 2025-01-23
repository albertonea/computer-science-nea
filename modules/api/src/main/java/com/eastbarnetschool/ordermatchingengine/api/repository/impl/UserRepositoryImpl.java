package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.Balance;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.UserRepository;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate template;
    private final BalancesService balancesService;

    public UserRepositoryImpl(NamedParameterJdbcTemplate template, BalancesService balancesService) {
        this.template = template;
        this.balancesService = balancesService;
    }

    @Override
    public UserWithBalancesResponse get(UUID userId) {
         return template.queryForObject(
            """
                 select
                     u.user_id,
                     u.username,
                     u.created_at,
                     json_agg(json_build_object(
                         'ticker', b.ticker,
                         'balance', b.balance,
                         'locked_balance', b.locked_balance
                     )) as balances 
                 from users u 
                 left join balances b on u.user_id = b.user_id 
                 where u.user_id = :userId 
                 group by u.user_id
                """,
                Map.of("userId", userId),
                (rs, rowNum) -> {
                    try {
                        return new UserWithBalancesResponse(
                                rs.getObject("user_id", UUID.class),
                                rs.getString("username"),
                                rs.getTimestamp("created_at"),
                                new ObjectMapper().readValue(rs.getString("balances" ), new TypeReference<List<Balance>>() {})
                        );
                    } catch (JsonProcessingException e) {
                        return new UserWithBalancesResponse(
                            rs.getObject("user_id", UUID.class),
                            rs.getString("username"),
                            rs.getTimestamp("created_at"),
                            new ArrayList<>()
                        );
                    }
                }
        );
    }

    @Override
    public UserWithBalancesResponse create(String username) {
        UserEntity user = template.queryForObject(
                "with inserted as (insert into users (username) values (:username) " +
                    "on conflict(username) do nothing returning user_id, username, created_at) " +
                    "select user_id, username, created_at from inserted union all select user_id, username, created_at " +
                    "from users where username = :username",
                Map.of("username", username),
                (rs, rowId) -> new UserEntity(rs.getObject("user_id", UUID.class), rs.getObject("username", String.class), rs.getObject("created_at", Timestamp.class)));

        BalanceEntity usdBalance = balancesService.updateOrCreateBalance(user.getUserId(), "USD", 100000L, 0L);
        BalanceEntity aaplBalance = balancesService.updateOrCreateBalance(user.getUserId(), "AAPL", 1000L, 0L);
        ArrayList<BalanceEntity> balances = new ArrayList<>();
        balances.add(usdBalance);
        balances.add(aaplBalance);

        return new UserWithBalancesResponse(user.getUserId(), user.getUsername(), user.getCreatedAt(), balances);
    }
}