package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.RefreshTokenEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.RefreshTokensRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokensService {
    private final RefreshTokensRepository refreshTokensRepository;

    public RefreshTokensService(RefreshTokensRepository refreshTokensRepository) {
        this.refreshTokensRepository = refreshTokensRepository;
    }

    public void save(RefreshTokenEntity refreshTokenEntity) {
        refreshTokensRepository.save(refreshTokenEntity);
    }

    public Optional<RefreshTokenEntity> findByIdAndUnexpired(UUID refreshToken) {
        return refreshTokensRepository.findByIdAndUnexpired(refreshToken);
    }

    public void deleteById(UUID refreshToken) {
        refreshTokensRepository.deleteById(refreshToken);
    }
}
