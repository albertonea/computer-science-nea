package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.BalanceDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserWithBalancesResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    private final BalancesService balancesService;

    public UserController(UserService userService, BalancesService balancesService) {
        this.userService = userService;
        this.balancesService = balancesService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<UserDashboardDto> getUserByUsername(final Authentication authentication) {
        final var user = userService.getDashboard(authentication.getName());
        return ResponseEntity.ok(user);
    }

//    @GetMapping("/balances")
//    public ResponseEntity<List<BalanceDto>> getUserBalances(final Authentication authentication) {
//        final var balances = balancesService.get(authentication.getName());
//    }
}
