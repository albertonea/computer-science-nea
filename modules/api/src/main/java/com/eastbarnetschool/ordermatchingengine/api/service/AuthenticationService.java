package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponseDto authenticate(final AuthenticationRequestDto request) {
        try {
            final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword());
            authenticationManager.authenticate(authToken);

            final var token = jwtService.generateToken(request.getUsername());
            return new AuthenticationResponseDto(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}