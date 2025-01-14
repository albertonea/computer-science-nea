package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.api.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.dto.OrderRequest;
import com.eastbarnetschool.ordermatchingengine.api.dto.TradeResponse;
import com.eastbarnetschool.ordermatchingengine.api.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.OpenOrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.domain.MatchingEngineResponse;
import com.eastbarnetschool.ordermatchingengine.domain.Order;
import com.eastbarnetschool.ordermatchingengine.domain.OrderGateway;
import com.eastbarnetschool.ordermatchingengine.domain.Trade;
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

    OrderController(OrderGateway orderGateway, SimpMessagingTemplate messagingTemplate, TradeService tradeService, OpenOrdersService openOrdersService) {
        this.orderGateway = orderGateway;
        this.messagingTemplate = messagingTemplate;
        this.tradeService = tradeService;
        this.openOrdersService = openOrdersService;
    }

    @MessageMapping("/order.place")
    public void placeOrder(OrderRequest order) {
        MatchingEngineResponse response = orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getTicker(), order.getSide(), order.getOrderType(), order.getUserId(), Instant.now()));

        messagingTemplate.convertAndSend("/stream/openOrders/" + order.getUserId(), order);
//      messagingTemplate.convertAndSend("/trades");

        //send order and match order generate trades
        //from trades send updates to each user for their open orders
        Order placedOrder = response.getPlacedOrder();
        openOrdersService.insert(new OrderEntity(Timestamp.from(placedOrder.getCreatedAt()), placedOrder.getPrice(), placedOrder.getTicker(), placedOrder.getRemainingQuantity(), placedOrder.getInitialQuantity(),  placedOrder.getUserId(), placedOrder.getOrderId(), placedOrder.getSide()));

        for ( Trade trade : response.getTrades() ) {
            messagingTemplate.convertAndSend("/stream/trades/" + trade.getTicker(), new TradeResponse(trade.getQuantity(), trade.getPrice()));
            tradeService.insert(new TradeEntity(trade.getSellerId(), trade.getTicker(), trade.getBuyerId(), trade.getQuantity(), trade.getPrice(), Timestamp.from(trade.getTradeTime()), trade.getTradeId()));
        }

        for ( Order filledOrder : response.getFilledOrders()) {
            messagingTemplate.convertAndSend("/stream/filledOrders/" + filledOrder.getUserId(), new FilledOrderResponse(filledOrder.getOrderId(), filledOrder.getPrice(), filledOrder.getInitialQuantity(), filledOrder.getTicker(), filledOrder.getSide(), filledOrder.getOrderType(), filledOrder.getCreatedAt()));
            openOrdersService.delete(filledOrder.getOrderId());
        }

    }
}