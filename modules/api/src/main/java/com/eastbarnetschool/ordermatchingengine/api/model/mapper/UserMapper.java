package com.eastbarnetschool.ordermatchingengine.api.model.mapper;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity toUserEntity(RegistrationRequestDto user) {
        return new UserEntity(UUID.randomUUID(), user.getUsername(), passwordEncoder.encode(user.getPassword()), Timestamp.from(Instant.now()));
    }
}
