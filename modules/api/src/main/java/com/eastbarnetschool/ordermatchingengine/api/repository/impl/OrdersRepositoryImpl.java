package com.eastbarnetschool.ordermatchingengine.api.repository.impl;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookEntryDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookLevelDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.rowmapper.OrderRowMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.OrdersRepository;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class OrdersRepositoryImpl implements OrdersRepository {
    private final NamedParameterJdbcTemplate template;
    public OrdersRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void delete(UUID orderId) {
        template.update(
                """
                    delete from open_orders
                    where order_id = :orderID
                    """,
                Map.of("orderID", orderId)
        );
    }

    @Override
    public void insertOrUpdate(OrderEntity order) {
        template.update(
            """
                insert into open_orders (order_id, user_id, side, ticker, remaining_quantity, initial_quantity, price, created_at)
                values (:orderId, :userId, :side, :ticker, :remainingQuantity, :initialQuantity, :price, :createdAt)
                on conflict (order_id)
                do update set remaining_quantity = :remainingQuantity
                """,
                order.toQueryParameters()
        );
    }

    @Override
    public List<OrderEntity> getOpenOrders(UUID userId, String ticker) {
        return template.query(
                """
                select *
                from open_orders
                where
                    user_id = :userId
                    and
                    ticker = :ticker
                    and 
                    remaining_quantity > 0
                """,
                Map.of("userId", userId, "ticker", ticker),
                new OrderRowMapper()
        );
    }

    @Override
    public List<OrderEntity> getAllOpenOrders() {
        return template.query(
                """
                select *
                from open_orders
                where remaining_quantity > 0
                """,
                new OrderRowMapper()
        );
    }

    @Override
    public List<OrderBookEntryDto> getAggregatedOrders() {
        return template.query(
                """
                select
                    ticker,
                    price,
                    side,
                    sum(remaining_quantity) as total_quantity
                from
                    open_orders
                group by ticker, price, side
                order by ticker, price desc;
                """,
                (rs, rowNum) -> new OrderBookEntryDto(
                        rs.getObject("ticker", String.class),
                        rs.getObject("price", Long.class),
                        Side.valueOf(rs.getString("side").toUpperCase()),
                        rs.getLong("total_quantity")
                )
        );
    }
}
