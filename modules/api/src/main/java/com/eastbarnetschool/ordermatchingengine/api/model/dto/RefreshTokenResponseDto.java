package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import java.time.Instant;
import java.util.UUID;

public class RefreshTokenResponseDto {
    private final String token;
    private final UUID refreshToken;

    public RefreshTokenResponseDto(String token, UUID refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
    public String getToken() {
        return token;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }
}
