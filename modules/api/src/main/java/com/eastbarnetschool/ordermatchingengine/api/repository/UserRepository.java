package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository {
    UserWithBalancesResponse get(UUID userId);
    UserWithBalancesResponse create(String username);
}
