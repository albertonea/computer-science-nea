package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    UserWithBalancesResponse getUserWithBalances(String username);
    Optional<UserEntity> getByUsername(String username);
    boolean exists(String username);
    void create(UserEntity userEntity);
    Optional<UserDashboardDto> getUserWithOrdersAndBalances(String username);
    Optional<UserEntity> getById(UUID userId);
}
