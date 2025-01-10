package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.api.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public UserWithBalancesResponse getUserByUsername(@PathVariable String username) {
        try {
            return userService.get(username);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @GetMapping("/login/{username}")
    public ResponseEntity<UserWithBalancesResponse> login(@PathVariable String username) {
        try {
            UserWithBalancesResponse user = userService.get(username);
            return ResponseEntity.ok(user);
        } catch (EmptyResultDataAccessException e) {
            UserWithBalancesResponse response = userService.create(username);
            return ResponseEntity.ok(response);
        }
    }
}
