package com.eastbarnetschool.ordermatchingengine.api.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table("refresh_tokens")
public class RefreshTokenEntity {
    @Id
    private final UUID id;
    private final UUID userId;
    private Timestamp createdAt;
    private final Timestamp expiresAt;

    public RefreshTokenEntity(UUID userId, Timestamp expiresAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public RefreshTokenEntity(UUID id, UUID userId, Timestamp createdAt, Timestamp expiresAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }
}
