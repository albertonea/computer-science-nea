package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.StopOrderEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StopOrdersRepository {
    void insert(StopOrderEntity stopOrder);
    void delete(UUID id);
}
