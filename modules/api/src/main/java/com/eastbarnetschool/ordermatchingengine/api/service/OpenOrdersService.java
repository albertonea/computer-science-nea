package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.OpenOrdersRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OpenOrdersService {
    private final OpenOrdersRepository openOrdersRepository;

    public OpenOrdersService(OpenOrdersRepository openOrdersRepository) {
        this.openOrdersRepository = openOrdersRepository;
    }

    public void insert(OrderEntity order) {
        openOrdersRepository.insert(order);
    }

    public void delete(UUID orderId) {
        openOrdersRepository.delete(orderId);
    }
}
