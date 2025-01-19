package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.api.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.dto.OrderRequest;
import com.eastbarnetschool.ordermatchingengine.api.dto.TradeResponse;
import com.eastbarnetschool.ordermatchingengine.api.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OpenOrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.domain.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

//@RestController()
//@RequestMapping(value = "/order")
@Controller
public class OrderController {
    private final OrderGateway orderGateway;
    private final SimpMessagingTemplate messagingTemplate;
    private final TradeService tradeService;
    private final OpenOrdersService openOrdersService;
    private final BalancesService balancesService;

    OrderController(OrderGateway orderGateway, SimpMessagingTemplate messagingTemplate, TradeService tradeService, OpenOrdersService openOrdersService, BalancesService balancesService) {
        this.orderGateway = orderGateway;
        this.messagingTemplate = messagingTemplate;
        this.tradeService = tradeService;
        this.openOrdersService = openOrdersService;
        this.balancesService = balancesService;
    }

    @MessageMapping("/order.place")
    public void placeOrder(OrderRequest order) {
        if (order.getSide() == Side.BUY) {
            Integer cost = Math.toIntExact(order.getQuantity() * order.getPrice());
            if (balancesService.checkIfHasEnoughBalance(order.getUserId(), "USD", cost)) {
                balancesService.updateOrCreateBalance(order.getUserId(), "USD", -cost, cost);
            } else {
                messagingTemplate.convertAndSend("/stream/errors/" + order.getUserId(), "Not enough balance. Ticker: " + order.getTicker() + " Balance Required: " + cost);
                return;
            }
        } else {
            if (balancesService.checkIfHasEnoughBalance(order.getUserId(), order.getTicker(), order.getQuantity())) {
                balancesService.updateOrCreateBalance(order.getUserId(), order.getTicker(), -order.getQuantity(), order.getQuantity());
            } else {
                messagingTemplate.convertAndSend("/stream/errors/" + order.getUserId(), "Not enough balance. Ticker: " + order.getTicker() + " Balance Required: " + order.getQuantity());
                return;
            }
        }

        MatchingEngineResponse response = orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getTicker(), order.getSide(), order.getOrderType(), order.getUserId(), Instant.now()));
        // change balance for user that placed order
        // place order in order book

        Order placedOrder = response.getPlacedOrder();
        OrderEntity placedOrderEntity = new OrderEntity(Timestamp.from(placedOrder.getCreatedAt()), placedOrder.getPrice(), placedOrder.getTicker(), placedOrder.getRemainingQuantity(), placedOrder.getInitialQuantity(),  placedOrder.getUserId(), placedOrder.getOrderId(), placedOrder.getSide());

        // return order opened
        messagingTemplate.convertAndSend("/stream/openOrders/" + order.getUserId(), placedOrderEntity);
        openOrdersService.insert(placedOrderEntity);

        for ( Trade trade : response.getTrades() ) {
            // change balances
            Integer cost = Math.toIntExact(trade.getQuantity() * trade.getPrice());
            balancesService.updateOrCreateBalance(trade.getSellerId(), trade.getTicker(), 0, -trade.getQuantity());
            balancesService.updateOrCreateBalance(trade.getSellerId(), "USD", cost, 0);

            balancesService.updateOrCreateBalance(trade.getBuyerId(), trade.getTicker(), trade.getQuantity(), 0);
            balancesService.updateOrCreateBalance(trade.getBuyerId(), "USD", 0, -cost);

            // return trades for ticker
            messagingTemplate.convertAndSend("/stream/trades/" + trade.getTicker(), new TradeResponse(trade.getQuantity(), trade.getPrice()));
            tradeService.insert(new TradeEntity(trade.getSellerId(), trade.getTicker(), trade.getBuyerId(), trade.getQuantity(), trade.getPrice(), Timestamp.from(trade.getTradeTime()), trade.getTradeId()));
        }

        for ( Order filledOrder : response.getFilledOrders() ) {
            messagingTemplate.convertAndSend("/stream/filledOrders/" + filledOrder.getUserId(), new FilledOrderResponse(filledOrder.getOrderId(), filledOrder.getPrice(), filledOrder.getInitialQuantity(), filledOrder.getTicker(), filledOrder.getSide(), filledOrder.getOrderType(), filledOrder.getCreatedAt()));
            openOrdersService.update(new OrderEntity(Timestamp.from(filledOrder.getCreatedAt()), filledOrder.getPrice(), filledOrder.getTicker(), filledOrder.getRemainingQuantity(), filledOrder.getInitialQuantity(), filledOrder.getUserId(), filledOrder.getOrderId(), filledOrder.getSide()));

            // return filled and partially filled orders
        }

    }
}