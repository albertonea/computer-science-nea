package com.eastbarnetschool.ordermatchingengine.api.repository;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.RefreshTokenEntity;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokensRepository {
    public void save(RefreshTokenEntity refreshTokenEntity);

    Optional<RefreshTokenEntity> findByIdAndUnexpired(UUID refreshToken);

    void deleteById(UUID refreshToken);
}
