package com.eastbarnetschool.ordermatchingengine.api.model.mapper;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class OrderMapper {
    public OrderEntity toEntity(Order order) {
        return new OrderEntity(
                Timestamp.from(order.getCreatedAt()),
                order.getPrice(),
                order.getTicker(),
                order.getRemainingQuantity(),
                order.getInitialQuantity(),
                order.getUserId(),
                order.getOrderId(),
                order.getSide(),
                order.getExecutedValue(),
                order.getOrderType()
        );
    }
    public FilledOrderResponse toFilledOrderResponse(Order order) {
        return new FilledOrderResponse(
                order.getOrderId(),
                order.getPrice(),
                order.getInitialQuantity(),
                order.getRemainingQuantity(),
                order.getTicker(),
                order.getSide(),
                order.getOrderType(),
                order.getCreatedAt()
        );
    }
}
