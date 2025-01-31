package com.eastbarnetschool.ordermatchingengine.api.runner;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.domain.Order;
import com.eastbarnetschool.ordermatchingengine.domain.OrderGateway;
import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderbookInitialiser implements ApplicationRunner {
    public final OrderGateway orderGateway;
    public final OrdersService ordersService;

    public OrderbookInitialiser(OrderGateway orderGateway, OrdersService ordersService) {
        this.orderGateway = orderGateway;
        this.ordersService = ordersService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Starting Order Matching Engine");
        List<OrderEntity> orders = ordersService.getAllOpenOrders();
        System.out.println("Found " + orders.size() + " Open Orders");
        orders.forEach(order -> orderGateway.placeOrder(new Order(order.getOrderId(), order.getPrice(), order.getInitialQuantity(), order.getRemainingQuantity(), order.getTicker(), order.getSide(), OrderType.LIMIT, order.getUserId(), order.getCreatedAt().toInstant())));
        System.out.println("Order Matching Engine has been started");
    }
}
