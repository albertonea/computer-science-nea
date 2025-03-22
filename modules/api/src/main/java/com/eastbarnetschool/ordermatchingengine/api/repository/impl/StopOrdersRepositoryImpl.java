package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.StopOrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.StopOrdersRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class StopOrdersRepositoryImpl implements StopOrdersRepository {
    private final NamedParameterJdbcTemplate template;

    public StopOrdersRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void insert(StopOrderEntity stopOrder) {
        template.update(
                """
                insert into stop_orders(id, trigger_price, order_id) values(:id, :triggerPrice, :orderId)
                """,
                Map.of("id", stopOrder.getId(), "triggerPrice", stopOrder.getTriggerPrice(), "orderId", stopOrder.getOrderId())
        );
    }

    @Override
    public void delete(UUID id) {
        template.update(
                """
                delete from stop_orders where id = :id
                """,
                Map.of("id", id)
        );
    }
}
