package com.eastbarnetschool.ordermatchingengine.api.model.entity.rowmapper;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class OrderRowMapper implements RowMapper<OrderEntity> {
    @Override
    public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderEntity(
                rs.getObject("created_at", Timestamp.class),
                rs.getObject("price", Long.class),
                rs.getObject("ticker", String.class),
                rs.getObject("remaining_quantity", Long.class),
                rs.getObject("initial_quantity", Long.class),
                rs.getObject("user_id", UUID.class),
                rs.getObject("order_id", UUID.class),
                Side.valueOf(rs.getString("side").toUpperCase())
        );
    }
}
