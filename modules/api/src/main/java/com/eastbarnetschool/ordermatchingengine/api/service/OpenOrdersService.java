package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.OpenOrdersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class OpenOrdersService {
    private final OpenOrdersRepository openOrdersRepository;

    public OpenOrdersService(OpenOrdersRepository openOrdersRepository) {
        this.openOrdersRepository = openOrdersRepository;
    }

    public void delete(UUID orderId) {
        openOrdersRepository.delete(orderId);
    }

    public void insertOrUpdate(OrderEntity order) {
        openOrdersRepository.insertOrUpdate(order);
    }

    public ArrayList<OrderEntity> getAllOpenOrders() {
        return openOrdersRepository.getAllOpenOpenOrders();
    }
}
