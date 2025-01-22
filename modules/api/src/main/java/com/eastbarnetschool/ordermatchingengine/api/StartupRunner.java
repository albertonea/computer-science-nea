package com.eastbarnetschool.ordermatchingengine.api;

import com.eastbarnetschool.ordermatchingengine.api.controllers.OrderController;
import com.eastbarnetschool.ordermatchingengine.api.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.service.OpenOrdersService;
import com.eastbarnetschool.ordermatchingengine.domain.Order;
import com.eastbarnetschool.ordermatchingengine.domain.OrderGateway;
import com.eastbarnetschool.ordermatchingengine.domain.OrderType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Component
public class StartupRunner implements ApplicationRunner {
    public final OrderGateway orderGateway;
    public final OpenOrdersService openOrdersService;

    public StartupRunner(OrderGateway orderGateway, OpenOrdersService openOrdersService) {
        this.orderGateway = orderGateway;
        this.openOrdersService = openOrdersService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Starting Order Matching Engine");
        ArrayList<OrderEntity> orders = openOrdersService.getAllOpenOrders();
        orders.forEach(order -> {
            orderGateway.placeOrder(new Order(order.getPrice(), order.getInitialQuantity(), order.getRemainingQuantity(), order.getTicker(), order.getSide(), OrderType.LIMIT, order.getUserId(), order.getCreatedAt().toInstant()));
        });
    }
}
