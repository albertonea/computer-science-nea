package com.eastbarnetschool.ordermatchingengine.api.controller.websocket;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.TradeMapper;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import com.eastbarnetschool.ordermatchingengine.domain.*;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
public class WsOrderController {
    private final OrderGateway orderGateway;
    private final SimpMessagingTemplate messagingTemplate;
    private final TradeService tradeService;
    private final OrdersService ordersService;
    private final BalancesService balancesService;
    private final UserService userService;
    private final TradeMapper tradeMapper;

    WsOrderController(OrderGateway orderGateway, SimpMessagingTemplate messagingTemplate, TradeService tradeService, OrdersService ordersService, BalancesService balancesService, UserService userService, TradeMapper tradeMapper) {
        this.orderGateway = orderGateway;
        this.messagingTemplate = messagingTemplate;
        this.tradeService = tradeService;
        this.ordersService = ordersService;
        this.balancesService = balancesService;
        this.userService = userService;
        this.tradeMapper = tradeMapper;
    }

    @MessageMapping("/order.place")
    public void placeOrder(OrderRequestDto order, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("user");

        if (username == null) {
            return;
        }
        UserEntity user = userService.getByUsername(username);

        if (order.getSide() == Side.BUY) {
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

        orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getQuantity(), order.getTicker(), order.getSide(), order.getOrderType(), user.getUserId(), Instant.now()));
    }

    @Scheduled(fixedRate = 5000)
    public void sendOrderbook() {
        List<OrderBookResponseDto> orders = ordersService.getAllOrderBooks();
        for (OrderBookResponseDto order : orders) {
            messagingTemplate.convertAndSend("/stream/orderBook/" + order.getTicker(), orders);
        }
    }
}