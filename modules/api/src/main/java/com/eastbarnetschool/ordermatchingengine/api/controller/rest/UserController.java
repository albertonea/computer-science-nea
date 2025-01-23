package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BalancesService balancesService;

    public UserController(UserService userService, BalancesService balancesService) {
        this.userService = userService;
        this.balancesService = balancesService;
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

    @GetMapping("/{userId}/balances")
    public ArrayList<BalanceEntity> getUserBalances(@PathVariable UUID userId) {
        try {
            return balancesService.get(userId);
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
