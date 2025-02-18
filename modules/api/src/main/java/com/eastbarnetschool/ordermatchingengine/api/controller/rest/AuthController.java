package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.*;
import com.eastbarnetschool.ordermatchingengine.api.service.AuthenticationService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody final AuthenticationRequestDto authenticationRequestDto) {
        Optional<AuthenticationResponseDto> optionalAuthRequest = authenticationService.authenticate(authenticationRequestDto);
        if (optionalAuthRequest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optionalAuthRequest.get());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody final RegistrationRequestDto registrationRequestDto) {
        boolean created = userService.registerUser(registrationRequestDto);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestParam UUID refreshToken) {
        Optional<RefreshTokenResponseDto> optionalRefreshToken = authenticationService.refreshToken(refreshToken);
        if (optionalRefreshToken.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalRefreshToken.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> revokeToken(@RequestParam UUID refreshToken) {
        authenticationService.revokeRefreshToken(refreshToken);
        return ResponseEntity.ok().build();
    }
}
