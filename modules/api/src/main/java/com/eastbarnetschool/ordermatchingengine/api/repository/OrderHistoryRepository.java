package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository {
    public void insert(OrderEntity orderEntity);
}
