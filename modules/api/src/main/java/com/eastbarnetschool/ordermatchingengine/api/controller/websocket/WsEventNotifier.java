package com.eastbarnetschool.ordermatchingengine.api.controller.websocket;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.TradeMapper;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.domain.events.*;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.TradingEventListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class WsEventNotifier implements TradingEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final TradeMapper tradeMapper;
    private final BalancesService balancesService;
    private final TradeService tradeService;
    private final OrdersService ordersService;

    public WsEventNotifier(SimpMessagingTemplate messagingTemplate, TradeMapper tradeMapper, BalancesService balancesService, TradeService tradeService, OrdersService ordersService) {
        this.messagingTemplate = messagingTemplate;
        this.tradeMapper = tradeMapper;
        this.balancesService = balancesService;
        this.tradeService = tradeService;
        this.ordersService = ordersService;
    }

    @Override
    public void onTrade(TradeEvent event) {
        var trade = event.getTrade();
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

    @Override
    public void onOrderFilledEvent(OrderFilledEvent event) {
        var filledOrder = event.getOrder();

        messagingTemplate.convertAndSend("/stream/filledOrders/" + filledOrder.getUserId(), new FilledOrderResponse(filledOrder.getOrderId(), filledOrder.getPrice(), filledOrder.getInitialQuantity(), filledOrder.getRemainingQuantity(), filledOrder.getTicker(), filledOrder.getSide(), filledOrder.getOrderType(), filledOrder.getCreatedAt()));
        ordersService.insertOrUpdate(new OrderEntity(Timestamp.from(filledOrder.getCreatedAt()), filledOrder.getPrice(), filledOrder.getTicker(), filledOrder.getRemainingQuantity(), filledOrder.getInitialQuantity(), filledOrder.getUserId(), filledOrder.getOrderId(), filledOrder.getSide()));
    }

    @Override
    public void onOrderPlacedEvent(OrderPlacedEvent event) {

        Order placedOrder = event.getOrder();
        OrderEntity placedOrderEntity = new OrderEntity(Timestamp.from(placedOrder.getCreatedAt()), placedOrder.getPrice(), placedOrder.getTicker(), placedOrder.getRemainingQuantity(), placedOrder.getInitialQuantity(),  placedOrder.getUserId(), placedOrder.getOrderId(), placedOrder.getSide());

        // return order opened
        messagingTemplate.convertAndSend("/stream/openOrders/" + placedOrder.getUserId(), placedOrderEntity);
        ordersService.insertOrUpdate(placedOrderEntity);
    }

    @Override
    public void onStopOrderQueuedEvent(StopOrderQueuedEvent event) {

    }

    @Override
    public void onStopOrderExecutedEvent(StopOrderExecutedEvent event) {

    }
}
