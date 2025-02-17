package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.OrderHistoryRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {
    private final NamedParameterJdbcTemplate template;
    public OrderHistoryRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void insert(OrderEntity orderEntity) {
        template.update(
            """
                insert into order_history(order_id, user_id, side, ticker, executed_value, remaining_quantity, initial_quantity, price, order_type, created_at)
                values (:orderId, :userId, :side, :ticker, :executedValue, :remainingQuantity, :initialQuantity, :price, :orderType, :createdAt)
                """,
                orderEntity.toQueryParameters()
        );
    }
}
