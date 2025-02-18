package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.UserMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserWithBalancesResponse getWithBalances(String username) {
        return userRepository.getUserWithBalances(username);
    }

    public Optional<UserDashboardDto> getDashboard(String username) {
        return userRepository.getUserWithOrdersAndBalances(username);
    }

    public Optional<UserEntity> getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public boolean registerUser(RegistrationRequestDto registrationRequestDto) {

        if (userRepository.exists(registrationRequestDto.getUsername())) {
            return false;
        }

        UserEntity userEntity = userMapper.toUserEntity(registrationRequestDto);
        userRepository.create(userEntity);
        return true;
    }

    public Optional<UserEntity> getById(UUID userId) {
        return userRepository.getById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username [%s] not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
