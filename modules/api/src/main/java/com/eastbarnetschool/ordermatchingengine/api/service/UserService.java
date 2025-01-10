package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserWithBalancesResponse get(String username) {
        return userRepository.get(username);
    }

    public UserWithBalancesResponse create(String username) {
        return userRepository.create(username);
    }
}
