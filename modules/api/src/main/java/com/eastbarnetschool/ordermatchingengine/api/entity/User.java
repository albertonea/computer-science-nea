package com.eastbarnetschool.ordermatchingengine.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table("users")
public class User {

    @Id
    private UUID userId;
    private String username;
    private Timestamp createdAt;

    public User(UUID userId, String username, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }
}
