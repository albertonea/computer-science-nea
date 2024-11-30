package com.eastbarnetschool.ordermatchingengine.api.controllers;

import com.eastbarnetschool.ordermatchingengine.domain.Order;
import com.eastbarnetschool.ordermatchingengine.domain.OrderGateway;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

//@RestController()
//@RequestMapping(value = "/order")
@Controller
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
    @SendTo("/stream/aggTrade")
    public String hello(String message) {
        return "hello" + message;
    }

    @MessageMapping("/order.place")
    @SendTo("/stream/trade")
    public OrderRequest placeOrder(OrderRequest order) {
        orderGateway.placeOrder(new Order(order.getPrice(), order.getQuantity(), order.getUsername(), order.getSide(), order.getOrderType()));
        return order;
    }
}
