package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface OpenOrdersRepository {
    void insert(OrderEntity order);
    void delete(UUID orderId);
    void update(OrderEntity order);
}
