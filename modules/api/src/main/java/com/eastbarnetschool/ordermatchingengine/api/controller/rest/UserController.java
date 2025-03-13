package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.UserDashboardDto;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<UserDashboardDto> getUserByUsername(final Authentication authentication) {
        Optional<UserDashboardDto> optionalDashboard = userService.getDashboard(authentication.getName());
        if (optionalDashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalDashboard.get());
    }
}
