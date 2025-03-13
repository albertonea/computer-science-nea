package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RefreshTokenResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.RefreshTokenEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final RefreshTokensService refreshTokensService;
    @Value("${jwt.refresh-token-ttl}")
    private Duration refreshTokenTtl;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, RefreshTokensService refreshTokensService, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokensService = refreshTokensService;
        this.userMapper = userMapper;
    }

    public Optional<AuthenticationResponseDto> authenticate(final AuthenticationRequestDto request) {
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(authToken);

        String token = jwtService.generateToken(request.getUsername());

        Optional<UserEntity> optionalUser = userService.getByUsername(request.getUsername());

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        UserEntity user = optionalUser.get();

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(
                user.getUserId(),
                Timestamp.from(Instant.now().plus(refreshTokenTtl))
        );

        refreshTokensService.save(refreshTokenEntity);

        return Optional.of(new AuthenticationResponseDto(token, refreshTokenEntity.getId(), refreshTokenEntity.getExpiresAt().toInstant(), userMapper.toUserDto(user)));
    }

    public Optional<RefreshTokenResponseDto> refreshToken(UUID refreshToken) {
        Optional<RefreshTokenEntity> optionalRefreshToken = refreshTokensService.findByIdAndUnexpired(refreshToken);
        if (optionalRefreshToken.isEmpty()) {
            return Optional.empty();
        }
        RefreshTokenEntity refreshTokenEntity = optionalRefreshToken.get();

        Optional<UserEntity> optionalUser = userService.getById(refreshTokenEntity.getUserId());
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        UserEntity user = optionalUser.get();

        String newAccessToken = jwtService.generateToken(user.getUsername());
        return Optional.of(new RefreshTokenResponseDto(newAccessToken, refreshToken));
    }

    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokensService.deleteById(refreshToken);
    }
}