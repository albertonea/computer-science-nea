package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository {
    UserWithBalancesResponse get(String username);
    boolean exists(String username);
    UserEntity create(UserEntity userEntity);
    UserDashboardDto getUserWithOrdersAndBalances(String username);
}
