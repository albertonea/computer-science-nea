package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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
    public void insert(OrderEntity order) {
        template.update(
            "insert into open_orders (order_id, user_id, side, ticker, remaining_quantity, initial_quantity, price, created_at)" +
                    " values (:orderId, :userId, :side, :ticker, :remainingQuantity, :initialQuantity, :price, :createdAt)",
                Map.of("orderId", order.getOrderId(), "userId", order.getUserId(), "side", order.getSide().name(), "ticker", order.getTicker(), "remainingQuantity", order.getRemainingQuantity(), "initialQuantity", order.getInitialQuantity(), "price", order.getPrice(), "createdAt", order.getCreatedAt()));
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
    public void update(OrderEntity order) {
        template.update(
                "insert into open_orders (order_id, user_id, side, ticker, remaining_quantity, initial_quantity, price, created_at)" +
                        " values (:orderId, :userId, :side, :ticker, :remainingQuantity, :initialQuantity, :price, :createdAt)" +
                        " on conflict (order_id)" +
                        " do update set remaining_quantity = :remainingQuantity",
                Map.of("orderId", order.getOrderId(), "userId", order.getUserId(), "side", order.getSide().name(), "ticker", order.getTicker(), "remainingQuantity", order.getRemainingQuantity(), "initialQuantity", order.getInitialQuantity(), "price", order.getPrice(), "createdAt", order.getCreatedAt()));
    }

    @Override
    public ArrayList<OrderEntity> getAllOpenOpenOrders() {
        return (ArrayList<OrderEntity>) template.query(
                "select * from open_orders where remaining_quantity > 0",
                (rs, rowId) -> new OrderEntity(rs.getObject("created_at", Timestamp.class), rs.getObject("price", Long.class), rs.getObject("ticker", String.class), rs.getObject("remaining_quantity", Integer.class), rs.getObject("initial_quantity", Integer.class), rs.getObject("user_id", UUID.class), rs.getObject("order_id", UUID.class), rs.getObject("side", Side.class))
        );
    }
}
