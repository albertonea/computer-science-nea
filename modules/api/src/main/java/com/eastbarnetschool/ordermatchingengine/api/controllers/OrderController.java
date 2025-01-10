package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.api.dto.OrderRequest;
import com.eastbarnetschool.ordermatchingengine.api.dto.TradeResponse;
import com.eastbarnetschool.ordermatchingengine.domain.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.ArrayList;

//@RestController()
//@RequestMapping(value = "/order")
@Controller
public class OrderController {
    private final OrderGateway orderGateway;
    private final SimpMessagingTemplate messagingTemplate;

    OrderController(OrderGateway orderGateway, SimpMessagingTemplate messagingTemplate) {
        this.orderGateway = orderGateway;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/order.place")
    public void placeOrder(OrderRequest order) {
        ArrayList<Trade> trades = orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getTicker(), order.getSide(), order.getOrderType(), order.getUserId(), Instant.now()));

//       messagingTemplate.convertAndSend("/trades");

        //send order and match order generate trades
        //from trades send updates to each user for their open orders

        for ( Trade trade : trades ) {
            messagingTemplate.convertAndSend("/stream/trades" + trade.getTicker(), new TradeResponse(trade.getQuantity(), trade.getPrice()));
            for (Order tradeOrder : trade.getOrders()) {
                messagingTemplate.convertAndSend("/stream/openOrders/" + tradeOrder.getUserId(), tradeOrder);
            }
        }

        messagingTemplate.convertAndSend("/stream", order);
    }
}