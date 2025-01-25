package com.eastbarnetschool.ordermatchingengine.api.model.mapper;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
public class UserMapper {
    public UserEntity toUserEntity(RegistrationRequestDto user) {
        return new UserEntity(UUID.randomUUID(), user.getUsername(), user.getPassword(), Timestamp.from(Instant.now()));
    }

    public RegistrationResponseDto toRegistrationResponse(UserEntity user) {
        return new RegistrationResponseDto(user.getUsername(), user.getPassword());
    }
}
