package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import java.util.UUID;

public class AuthenticationResponseDto {
    private final String token;
    private final UUID refreshToken;

    public AuthenticationResponseDto(String token, UUID refreshToken) {
        this.refreshToken = refreshToken;
        this.token = token;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }

    public String getToken() {
        return token;
    }
}