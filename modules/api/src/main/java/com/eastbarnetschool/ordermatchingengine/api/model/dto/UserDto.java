package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class UserDto {
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;

    public UserDto(UUID userId, String username, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
