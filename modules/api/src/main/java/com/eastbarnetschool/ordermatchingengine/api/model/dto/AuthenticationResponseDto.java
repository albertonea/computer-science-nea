package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class AuthenticationResponseDto {
    private final String token;
    private final UUID refreshToken;
    private final Instant expiresAt;
    private final UserDto user;

    public AuthenticationResponseDto(String token, UUID refreshToken, Instant expiresAt, UserDto user) {
        this.refreshToken = refreshToken;
        this.token = token;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getToken() {
        return token;
    }
}