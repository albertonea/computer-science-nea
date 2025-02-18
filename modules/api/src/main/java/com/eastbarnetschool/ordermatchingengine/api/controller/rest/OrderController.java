package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final UserService userService;
    private final OrdersService ordersService;

    public OrderController(UserService userService, OrdersService ordersService) {
        this.userService = userService;
        this.ordersService = ordersService;
    }

    @GetMapping("/open-orders/{ticker}")
    public ResponseEntity<List<OrderEntity>> getOpenOrders(@PathVariable String ticker, Authentication authentication) {
        Optional<UserEntity> optionalUser = userService.getByUsername(authentication.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = optionalUser.get();
        return ResponseEntity.ok(ordersService.getOpenOrders(user.getUserId(), ticker));
    }
}
