package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.OrderRowMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.OpenOrdersRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Repository
public class OpenOrdersRepositoryImpl implements OpenOrdersRepository {
    private final NamedParameterJdbcTemplate template;
    public OpenOrdersRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void delete(UUID orderId) {
        template.update(
                "delete from open_orders " +
                    "where order_id = :orderID",
                Map.of("orderID", orderId)
        );
    }

    @Override
    public void insertOrUpdate(OrderEntity order) {
        template.update(
                "insert into open_orders (order_id, user_id, side, ticker, remaining_quantity, initial_quantity, price, created_at)" +
                        " values (:orderId, :userId, :side, :ticker, :remainingQuantity, :initialQuantity, :price, :createdAt)" +
                        " on conflict (order_id)" +
                        " do update set remaining_quantity = :remainingQuantity",
                order.toQueryParameters()
        );
    }

    @Override
    public ArrayList<OrderEntity> getAllOpenOpenOrders() {
        return (ArrayList<OrderEntity>) template.query(
                "select * from open_orders where remaining_quantity > 0",
                new OrderRowMapper()
        );
    }
}
