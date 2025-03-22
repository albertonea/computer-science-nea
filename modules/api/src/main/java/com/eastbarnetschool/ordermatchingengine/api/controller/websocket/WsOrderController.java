package com.eastbarnetschool.ordermatchingengine.api.controller.websocket;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.OrderMapper;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import com.eastbarnetschool.ordermatchingengine.domain.*;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class WsOrderController {
    private final OrderGateway orderGateway;
    private final SimpMessagingTemplate messagingTemplate;
    private final OrdersService ordersService;
    private final BalancesService balancesService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    WsOrderController(OrderGateway orderGateway, SimpMessagingTemplate messagingTemplate, OrdersService ordersService, BalancesService balancesService, UserService userService, OrderMapper orderMapper) {
        this.orderGateway = orderGateway;
        this.messagingTemplate = messagingTemplate;
        this.ordersService = ordersService;
        this.balancesService = balancesService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @MessageMapping("/order.place")
    public void placeOrder(OrderRequestDto order, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("user");

        if (username == null) {
            return;
        }
        Optional<UserEntity> optionalUser = userService.getByUsername(username);
        if (optionalUser.isEmpty()) {
            messagingTemplate.convertAndSend("/stream/errors/" + username, "User not found");
            return;
        }

        UserEntity user = optionalUser.get();

        Side side = order.getSide();
        OrderType orderType = order.getOrderType();

        if (orderType == OrderType.LIMIT || orderType == OrderType.STOPLIMIT) {
            if (side == Side.BUY) {
                Long cost = (long) Math.toIntExact(order.getQuantity() * order.getPrice());
                if (balancesService.checkIfHasEnoughBalance(user.getUserId(), "USD", cost)) {
                    balancesService.updateOrCreateBalance(user.getUserId(), "USD", -cost, cost);
                } else {
                    messagingTemplate.convertAndSend("/stream/errors/" + user.getUserId(), "Not enough balance. Ticker: " + order.getTicker() + " Balance Required: " + cost);
                    return;
                }
            } else {
                if (balancesService.checkIfHasEnoughBalance(user.getUserId(), order.getTicker(), order.getQuantity())) {
                    balancesService.updateOrCreateBalance(user.getUserId(), order.getTicker(), -order.getQuantity(), order.getQuantity());
                } else {
                    messagingTemplate.convertAndSend("/stream/errors/" + user.getUserId(), "Not enough balance. Ticker: " + order.getTicker() + " Balance Required: " + order.getQuantity());
                    return;
                }
            }
        }

        if (orderType == OrderType.LIMIT || orderType == OrderType.MARKET) {
            orderGateway.placeOrder(orderMapper.toOrder(order, user.getUserId()));
        } else if (orderType == OrderType.STOPLIMIT || orderType == OrderType.STOPMARKET) {
            orderGateway.placeStopOrder(new StopOrder(order.getTriggerPrice(), orderMapper.toOrder(order, user.getUserId())));
        } else {
            messagingTemplate.convertAndSend("/stream/errors/" + user.getUserId(), "Incorrect order type");
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendOrderbook() {
        List<OrderBookResponseDto> orders = ordersService.getAllOrderBooks();
        for (OrderBookResponseDto order : orders) {
            messagingTemplate.convertAndSend("/stream/orderBook/" + order.getTicker(), orders);
        }
    }
}