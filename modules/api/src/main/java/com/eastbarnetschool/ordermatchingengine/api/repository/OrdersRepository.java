package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookEntryDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository {
    void delete(UUID orderId);
    void insertOrUpdate(OrderEntity order);
    List<OrderEntity> getOpenOrders(UUID userId, String ticker);
    List<OrderEntity> getAllOpenOrders();
    List<OrderBookEntryDto> getAggregatedOrders();
}
