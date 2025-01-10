package com.eastbarnetschool.ordermatchingengine.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table("users")
public class User {

    @Id
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;

    public User(UUID userId, String username, Timestamp createdAt) {
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
