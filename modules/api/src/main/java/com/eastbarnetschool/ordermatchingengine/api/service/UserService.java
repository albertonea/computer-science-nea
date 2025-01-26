package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.exception.ValidationException;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.UserMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserWithBalancesResponse get(String username) {
        return userRepository.get(username);
    }

    public UserDashboardDto getDashboard(String username) {
        return userRepository.getUserWithOrdersAndBalances(username);
    }



    public void registerUser(RegistrationRequestDto registrationRequestDto) {
        final var errors = new HashMap<String, String>();

        if (userRepository.exists(registrationRequestDto.getUsername())) {
            errors.put("username", "Username [%s] is already taken".formatted(registrationRequestDto.getUsername()));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(CONFLICT, errors);
        }
        final var userEntity = userMapper.toUserEntity(registrationRequestDto);
        userRepository.create(userEntity);
    }
}
