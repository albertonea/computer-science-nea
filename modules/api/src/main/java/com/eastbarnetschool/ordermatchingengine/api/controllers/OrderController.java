package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.domain.Order;
import com.eastbarnetschool.ordermatchingengine.domain.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController()
//@RequestMapping(value = "/order")
@Controller
@Slf4j
public class OrderController {
    private final OrderGateway orderGateway;

    OrderController(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

//    @PostMapping("/place")
//    public String hello(@RequestBody OrderRequest orderRequest) {
//        orderGateway.placeOrder(new Order(orderRequest.getPrice(), orderRequest.getQuantity(), orderRequest.getTicker(), orderRequest.getSide(), orderRequest.getOrderType()));
//        return "Order received";
//    }

    @MessageMapping("/order.hello")
    @SendTo("/stream")
    public String hello(String message) {
        return "hello" + message;
    }

    @MessageMapping("/order.place")
    @SendTo("/stream")
    public OrderRequest placeOrder(OrderRequest order) {
        orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getUsername(), order.getSide(), order.getOrderType()));
        return order;
    }
}
