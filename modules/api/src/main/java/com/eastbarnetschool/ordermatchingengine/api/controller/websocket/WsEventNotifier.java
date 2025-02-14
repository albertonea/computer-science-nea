package com.eastbarnetschool.ordermatchingengine.api.controller.websocket;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.FilledOrderResponse;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.TradeEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.OrderMapper;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.StopOrderMapper;
import com.eastbarnetschool.ordermatchingengine.api.model.mapper.TradeMapper;
import com.eastbarnetschool.ordermatchingengine.api.service.BalancesService;
import com.eastbarnetschool.ordermatchingengine.api.service.OrdersService;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import com.eastbarnetschool.ordermatchingengine.domain.Trade;
import com.eastbarnetschool.ordermatchingengine.domain.events.*;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.TradingEventListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;
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
    private final OrderMapper orderMapper;
    private final StopOrderMapper stopOrderMapper;

    public WsEventNotifier(SimpMessagingTemplate messagingTemplate, TradeMapper tradeMapper, BalancesService balancesService, TradeService tradeService, OrdersService ordersService, OrderMapper orderMapper, StopOrderMapper stopOrderMapper) {
        this.messagingTemplate = messagingTemplate;
        this.tradeMapper = tradeMapper;
        this.balancesService = balancesService;
        this.tradeService = tradeService;
        this.ordersService = ordersService;
        this.orderMapper = orderMapper;
        this.stopOrderMapper = stopOrderMapper;
    }

    @Override
    public void onTrade(TradeEvent event) {
        Trade trade = event.getTrade();
        // change balances
        Long cost = (long) Math.toIntExact(trade.getQuantity() * trade.getPrice());
        balancesService.updateOrCreateBalance(trade.getSellerId(), trade.getTicker(), 0L, -trade.getQuantity());
        balancesService.updateOrCreateBalance(trade.getSellerId(), "USD", cost, 0L);

        balancesService.updateOrCreateBalance(trade.getBuyerId(), trade.getTicker(), trade.getQuantity(), 0L);
        balancesService.updateOrCreateBalance(trade.getBuyerId(), "USD", 0L, -cost);

        // return trades for ticker
        tradeService.insert(new TradeEntity(trade.getSellerId(), trade.getTicker(), trade.getBuyerId(), trade.getQuantity(), trade.getPrice(), Timestamp.from(trade.getTradeTime()), trade.getTradeId()));
        messagingTemplate.convertAndSend("/stream/trades/" + trade.getTicker(), tradeMapper.toTradeDto(trade));
    }

    @Override
    public void onOrderFilledEvent(OrderFilledEvent event) {
        Order filledOrder = event.getOrder();
        ordersService.insertOrUpdate(orderMapper.toEntity(filledOrder));

        if (filledOrder.isFilled() || filledOrder.isMarketOrder()) {
            ordersService.moveToHistory(filledOrder.getOrderId());
        }

        messagingTemplate.convertAndSend("/stream/filledOrders/" + filledOrder.getUserId(), orderMapper.toFilledOrderResponse(filledOrder));
    }

    @Override
    public void onOrderPlacedEvent(OrderPlacedEvent event) {
        Order placedOrder = event.getOrder();
        OrderEntity placedOrderEntity = orderMapper.toEntity(placedOrder);

        ordersService.insertOrUpdate(placedOrderEntity);
        messagingTemplate.convertAndSend("/stream/openOrders/" + placedOrder.getUserId(), placedOrderEntity);
    }

    @Override
    public void onStopOrderQueuedEvent(StopOrderQueuedEvent event) {
        StopOrder stopOrder = event.getStopOrder();
        OrderEntity underlyingOrder = orderMapper.toEntity(stopOrder.getOrder());
        ordersService.insertOrUpdate(underlyingOrder);
        ordersService.insertStopOrder(stopOrderMapper.toEntity(stopOrder));
    }

    @Override
    public void onStopOrderExecutedEvent(StopOrderExecutedEvent event) {

    }
}
