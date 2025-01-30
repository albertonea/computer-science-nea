package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.*;
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
    public UserWithBalancesResponse getUserWithBalances(String username) {
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
    public Optional<UserEntity> getByUsername(String username) {
        return Optional.ofNullable(template.queryForObject(
                """
                        select * from users where username = :username
                        """,
                Map.of("username", username),
                new UserRowMapper()
        ));
    }

    @Override
    public UserDashboardDto getUserWithOrdersAndBalances(String username) {
    return template.queryForObject(
            """
                 SELECT
                     u.user_id as user_id,
                     u.username as username,
                     u.created_at as created_at,
                     coalesce(
                         (select json_agg(json_build_object(
                             'ticker', b.ticker,
                             'balance', b.balance,
                             'lockedBalance', b.locked_balance
                         )) from balances b where b.user_id = u.user_id),
                         '[]'::json
                     ) as balances,
                     coalesce(
                         (select json_agg(json_build_object(
                             'orderId', o.order_id,
                             'side', o.side,
                             'ticker', o.ticker,
                             'remainingQuantity', o.remaining_quantity,
                             'initialQuantity', o.initial_quantity,
                             'price', o.price,
                             'createdAt', o.created_at
                         )) from open_orders o where o.user_id = u.user_id),
                         '[]'::json
                     ) as orders,
                     coalesce(
                         (select json_agg(json_build_object(
                             'tradeId', t.trade_id,
                             'buy', case when t.buyer_id = u.user_id then true else false end,
                             'price', t.price,
                             'quantity', t.quantity,
                             'ticker', t.ticker,
                             'tradeTime', t.trade_time
                         )) from trades t where t.seller_id = u.user_id or t.buyer_id = u.user_id),
                         '[]'::json
                     ) as trades
                 from users u
                 where u.username = :username;
                """,
                Map.of("username", username),
                (rs, rowNum) -> {
                    String ordersJson = rs.getString("orders");
                    String balancesJson = rs.getString("balances");
                    String tradesJson = rs.getString("trades");

                    List<OrderDto> orders = new ArrayList<>();
                    List<BalanceDto> balances = new ArrayList<>();
                    List<TradeDto> trades = new ArrayList<>();

                    if (ordersJson != null && !ordersJson.isEmpty()) {
                        try {
                            orders = new ObjectMapper().readValue(ordersJson, new TypeReference<List<OrderDto>>() {});
                        } catch (JsonProcessingException e) {
                            System.err.println("Error parsing orders JSON: " + e.getMessage());
                        }
                    }

                    if (balancesJson != null && !balancesJson.isEmpty()) {
                        try {
                            balances = new ObjectMapper().readValue(balancesJson, new TypeReference<List<BalanceDto>>() {});
                        } catch (JsonProcessingException e) {
                            // Log the error or handle it as needed
                            System.err.println("Error parsing balances JSON: " + e.getMessage());
                        }
                    }

                    if (tradesJson != null && !tradesJson.isEmpty()) {
                        try {
                            trades = new ObjectMapper().readValue(tradesJson, new TypeReference<List<TradeDto>>() {});
                        } catch (JsonProcessingException e) {
                            // Log the error or handle it as needed
                            System.err.println("Error parsing balances JSON: " + e.getMessage());
                        }
                    }

                    return new UserDashboardDto(
                        rs.getObject("user_id", UUID.class),
                        rs.getString("username"),
                        rs.getTimestamp("created_at"),
                        balances,
                        orders,
                        trades
                    );
                }
        );
    }

    @Override
    public UserEntity getById(UUID userId) {
        return (template.queryForObject(
                """
                        select * from users where user_id = :userId
                        """,
                Map.of("userId", userId),
                new UserRowMapper()
        ));
    }

    @Override
    public boolean exists(String username) {
        return Boolean.TRUE.equals(template.queryForObject(
            """
                select exists (select 1 from users where username = :username)
                """,
                Map.of("username", username),
                Boolean.class
        ));
    }

    @Override
    public void create(UserEntity userEntity) {
        UUID userId = template.queryForObject(
            """
                insert into users (user_id, username, password, created_at)
                values (:userId, :username, :password, :createdAt)
                returning user_id
                """,
                Map.of(
                    "userId", userEntity.getUserId(),
                    "username", userEntity.getUsername(),
                    "password", userEntity.getPassword(),
                    "createdAt", userEntity.getCreatedAt()
                ),
                UUID.class
        );

        balancesService.updateOrCreateBalance(userId, "USD", 100000L, 0L);
        balancesService.updateOrCreateBalance(userId, "AAPL", 1000L, 0L);
    }


}