package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.AuthenticationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.RegistrationResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.service.AuthenticationService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody final RegistrationRequestDto registrationRequestDto) {
        userService.registerUser(registrationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@RequestParam UUID refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> revokeToken(@RequestParam UUID refreshToken) {
        authenticationService.revokeRefreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
