package com.eastbarnetschool.ordermatchingengine.api.controller.websocket;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.StopOrderRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.OrderMapper;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.TradeMapper;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import com.eastbarnetschool.ordermatchingengine.domain.*;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;
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
        UserEntity user = userService.getByUsername(username);

        if (order.getSide() == Side.BUY && order.getOrderType() == OrderType.LIMIT) {
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

        orderGateway.placeOrder(orderMapper.toOrder(order, user.getUserId()));
    }

    @MessageMapping("/stopOrder.place")
    public void placeStopOrder(StopOrderRequestDto order, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("user");

        if (username == null) {
            return;
        }
        UserEntity user = userService.getByUsername(username);

        if (order.getSide() == Side.BUY && order.getOrderType() == OrderType.STOPLIMIT) {
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

        Order newOrder = new Order(order.getPrice(), order.getQuantity(), order.getQuantity(), order.getTicker(), order.getSide(), order.getOrderType(), user.getUserId(), Instant.now());

        orderGateway.placeStopOrder(new StopOrder(order.getExecutionPrice(), newOrder));
    }

    @Scheduled(fixedRate = 5000)
    public void sendOrderbook() {
        List<OrderBookResponseDto> orders = ordersService.getAllOrderBooks();
        for (OrderBookResponseDto order : orders) {
            messagingTemplate.convertAndSend("/stream/orderBook/" + order.getTicker(), orders);
        }
    }
}