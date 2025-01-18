package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.entity.Balance;
import com.eastbarnetschool.ordermatchingengine.api.entity.User;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public UserWithBalancesResponse get(String username) {
        User user = template.queryForObject("select * from users where username = :username",
                Map.of("username", username),
                (rs, rowNum) -> new User(rs.getObject("user_id", UUID.class), rs.getObject("username", String.class), rs.getObject("created_at", Timestamp.class)));

        ArrayList<Balance> balances = balancesService.get(user.getUserId());
        return new UserWithBalancesResponse(user.getUserId(), user.getUsername(), user.getCreatedAt(), balances);
    }

    @Override
    public UserWithBalancesResponse create(String username) {
        User user = template.queryForObject("with inserted as (insert into users (username) values (:username) on conflict(username) do nothing returning user_id, username, created_at) select user_id, username, created_at from inserted union all select user_id, username, created_at from users where username = :username",
                Map.of("username", username),
                (rs, rowId) -> new User(rs.getObject("user_id", UUID.class), rs.getObject("username", String.class), rs.getObject("created_at", Timestamp.class)));

        Balance balance = balancesService.updateOrCreateBalance(user.getUserId(), "USD", 100000, 0);
        ArrayList<Balance> balances = new ArrayList<>();
        balances.add(balance);

        return new UserWithBalancesResponse(user.getUserId(), user.getUsername(), user.getCreatedAt(), balances);
    }
}