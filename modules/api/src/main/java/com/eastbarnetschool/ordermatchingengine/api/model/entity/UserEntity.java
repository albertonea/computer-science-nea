package com.eastbarnetschool.ordermatchingengine.api.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Table("users")
public class UserEntity {

    @Id
    private final UUID userId;
    private final String username;
    private final Timestamp createdAt;
    private final String password;

    public UserEntity(UUID userId, String username, String password, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
