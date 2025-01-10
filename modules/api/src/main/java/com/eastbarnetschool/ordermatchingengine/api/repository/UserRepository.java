package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.dto.UserWithBalancesResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    UserWithBalancesResponse get(String username);
    UserWithBalancesResponse create(String username);
}
