package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.BalanceEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/balances")
public class BalancesController {
    private final BalancesService balancesService;
    private final UserService userService;

    public BalancesController(BalancesService balancesService, UserService userService) {
        this.balancesService = balancesService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<BalanceEntity>> getUserBalances(final Authentication authentication) {
        Optional<UserEntity> optionalUser = userService.getByUsername(authentication.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = optionalUser.get();

        return ResponseEntity.ok(balancesService.getByUserId(user.getUserId()));
    }
}
