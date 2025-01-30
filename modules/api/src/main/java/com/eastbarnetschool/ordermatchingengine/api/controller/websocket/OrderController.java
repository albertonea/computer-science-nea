package com.eastbarnetschool.ordermatchingengine.api.controller.websocket;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderRequestDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.UserEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.TradeMapper;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OpenOrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.api.service.UserService;
import com.eastbarnetschool.ordermatchingengine.domain.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.Instant;

@Controller
public class OrderController {
    private final OrderGateway orderGateway;
    private final SimpMessagingTemplate messagingTemplate;
    private final TradeService tradeService;
    private final OpenOrdersService openOrdersService;
    private final BalancesService balancesService;
    private final UserService userService;
    private final TradeMapper tradeMapper;

    OrderController(OrderGateway orderGateway, SimpMessagingTemplate messagingTemplate, TradeService tradeService, OpenOrdersService openOrdersService, BalancesService balancesService, UserService userService, TradeMapper tradeMapper) {
        this.orderGateway = orderGateway;
        this.messagingTemplate = messagingTemplate;
        this.tradeService = tradeService;
        this.openOrdersService = openOrdersService;
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

        MatchingEngineResponse response = orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getQuantity(), order.getTicker(), order.getSide(), order.getOrderType(), user.getUserId(), Instant.now()));
        // change balance for user that placed order
        // place order in order book

        Order placedOrder = response.getPlacedOrder();
        OrderEntity placedOrderEntity = new OrderEntity(Timestamp.from(placedOrder.getCreatedAt()), placedOrder.getPrice(), placedOrder.getTicker(), placedOrder.getRemainingQuantity(), placedOrder.getInitialQuantity(),  placedOrder.getUserId(), placedOrder.getOrderId(), placedOrder.getSide());

        // return order opened
        messagingTemplate.convertAndSend("/stream/openOrders/" + user.getUserId(), placedOrderEntity);
        openOrdersService.insertOrUpdate(placedOrderEntity);

        for ( Trade trade : response.getTrades() ) {
            // change balances
            Long cost = (long) Math.toIntExact(trade.getQuantity() * trade.getPrice());
            balancesService.updateOrCreateBalance(trade.getSellerId(), trade.getTicker(), 0L, -trade.getQuantity());
            balancesService.updateOrCreateBalance(trade.getSellerId(), "USD", cost, 0L);

            balancesService.updateOrCreateBalance(trade.getBuyerId(), trade.getTicker(), trade.getQuantity(), 0L);
            balancesService.updateOrCreateBalance(trade.getBuyerId(), "USD", 0L, -cost);

            // return trades for ticker
            messagingTemplate.convertAndSend("/stream/trades/" + trade.getTicker(), tradeMapper.toTradeDto(trade));
            tradeService.insert(new TradeEntity(trade.getSellerId(), trade.getTicker(), trade.getBuyerId(), trade.getQuantity(), trade.getPrice(), Timestamp.from(trade.getTradeTime()), trade.getTradeId()));
        }

        for ( Order filledOrder : response.getFilledOrders() ) {
            messagingTemplate.convertAndSend("/stream/filledOrders/" + filledOrder.getUserId(), new FilledOrderResponse(filledOrder.getOrderId(), filledOrder.getPrice(), filledOrder.getInitialQuantity(), filledOrder.getTicker(), filledOrder.getSide(), filledOrder.getOrderType(), filledOrder.getCreatedAt()));
            openOrdersService.insertOrUpdate(new OrderEntity(Timestamp.from(filledOrder.getCreatedAt()), filledOrder.getPrice(), filledOrder.getTicker(), filledOrder.getRemainingQuantity(), filledOrder.getInitialQuantity(), filledOrder.getUserId(), filledOrder.getOrderId(), filledOrder.getSide()));

            // return filled and partially filled orders
        }
    }
}