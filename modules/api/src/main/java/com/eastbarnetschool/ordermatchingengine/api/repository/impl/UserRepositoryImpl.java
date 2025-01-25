package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.BalanceDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.rowmapper.UserRowMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.UserRepository;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
                 where u.username = :username
                 group by u.user_id
                """,
                Map.of("username", username),
                (rs, rowNum) -> {
                    List<BalanceDto> balances = new ArrayList<>();

                    try {
                        balances = new ObjectMapper().readValue(rs.getString("balances" ), new TypeReference<List<BalanceDto>>() {});
                    } catch (JsonProcessingException e) {}

                    return new UserWithBalancesResponse(
                        rs.getObject("user_id", UUID.class),
                        rs.getString("username"),
                        rs.getTimestamp("created_at"),
                        balances
                    );
                }
        );
    }

    @Override
    public UserDashboardDto getUserWithOrdersAndBalances(String username) {
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
                     )) as balances,
                    json_agg(json_build_object(
                        'order_id', o.order_id,
                        'side', o.side,
                        'ticker', o.ticker,
                        'remaining_quantity', o.remaining_quantity,
                        'initial_quantity', o.initial_quantity,
                        'price', o.price,
                        'created_at', o.created_at,
                    )) as orders
                 from users u
                 left join balances b on u.user_id = b.user_id
                 left join open_orders o on o.user_id = u.user_id
                 where u.username = :username
                 group by u.user_id
                """,
                Map.of("username", username),
                (rs, rowNum) -> {
                    List<OrderDto> orders =  new ArrayList<>();
                    List<BalanceDto> balances = new ArrayList<>();

                    try {
                        orders = new ObjectMapper().readValue(rs.getString("orders" ), new TypeReference<List<OrderDto>>() {});
                    } catch (JsonProcessingException e) {}

                    try {
                        balances = new ObjectMapper().readValue(rs.getString("balances" ), new TypeReference<List<BalanceDto>>() {});
                    } catch (JsonProcessingException e) {}

                    return new UserDashboardDto(
                        rs.getObject("user_id", UUID.class),
                        rs.getString("username"),
                        rs.getTimestamp("created_at"),
                        balances,
                        orders
                    );
                }
        );
    }

    @Override
    public boolean exists(String username) {
        return Boolean.TRUE.equals(template.queryForObject(
            """
                select username from users where username = :username
                """,
                Map.of("username", username),
                (rs, rowNum) -> rs.getString("username").equals(username)
        ));
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        UserEntity user = template.queryForObject(
            """
                insert into users (user_id, username, password, created_at)
                values (:userId, :username, :password, :createdAt)
                on conflict(username) do nothing returning user_id, username, created_at
                """,
                Map.of(
                    "userId", userEntity.getUserId(),
                    "username", userEntity.getUsername(),
                    "password", userEntity.getPassword(),
                    "createdAt", userEntity.getCreatedAt()
                ),
                new UserRowMapper()
        );

        balancesService.updateOrCreateBalance(user.getUserId(), "USD", 100000L, 0L);
        balancesService.updateOrCreateBalance(user.getUserId(), "AAPL", 1000L, 0L);
        return user;
    }


}