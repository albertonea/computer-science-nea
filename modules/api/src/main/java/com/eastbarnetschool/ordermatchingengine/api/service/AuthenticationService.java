package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RefreshTokenResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.RefreshTokenEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.UserMapper;
import com.eastbarnetschool.ordermatchingengine.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
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

    public AuthenticationResponseDto authenticate(final AuthenticationRequestDto request) {
        try {
            final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword());
            authenticationManager.authenticate(authToken);

            final var token = jwtService.generateToken(request.getUsername());

            final UserEntity user = userService.getByUsername(request.getUsername());

            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(
                    user.getUserId(),
                    Timestamp.from(Instant.now().plus(refreshTokenTtl))
            );

            refreshTokensService.save(refreshTokenEntity);

            return new AuthenticationResponseDto(token, refreshTokenEntity.getId(), refreshTokenEntity.getExpiresAt().toInstant(), userMapper.toUserDto(user));
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public RefreshTokenResponseDto refreshToken(UUID refreshToken) {
        final var refreshTokenEntity = refreshTokensService.findByIdAndUnexpired(refreshToken);
        final var user = userService.getById(refreshTokenEntity.getUserId());

        final var newAccessToken = jwtService.generateToken(user.getUsername());
        return new RefreshTokenResponseDto(newAccessToken, refreshToken);
    }

    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokensService.deleteById(refreshToken);
    }
}