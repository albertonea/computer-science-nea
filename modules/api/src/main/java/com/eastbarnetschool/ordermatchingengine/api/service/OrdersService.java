package com.eastbarnetschool.ordermatchingengine.api.service;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookEntryDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookLevelDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.OrderBookResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.OrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.model.entity.StopOrderEntity;
import com.eastbarnetschool.ordermatchingengine.api.repository.OrdersRepository;
import com.eastbarnetschool.ordermatchingengine.api.repository.StopOrdersRepository;
import com.eastbarnetschool.ordermatchingengine.api.repository.impl.StopOrdersRepositoryImpl;
import com.eastbarnetschool.ordermatchingengine.domain.Side;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final StopOrdersRepository stopOrdersRepository;

    public OrdersService(OrdersRepository ordersRepository, StopOrdersRepository stopOrdersRepository) {
        this.ordersRepository = ordersRepository;
        this.stopOrdersRepository = stopOrdersRepository;
    }

    public List<OrderEntity> getOpenOrders(UUID userId, String ticker) {
        return ordersRepository.getOpenOrders(userId, ticker);
    }

    public void insertOrUpdate(OrderEntity order) {
        ordersRepository.insertOrUpdate(order);
    }

    public List<OrderEntity> getAllOpenOrders() {
        return ordersRepository.getAllOpenOrders();
    }

    public List<OrderBookResponseDto> getAllOrderBooks() {
        List<OrderBookEntryDto> orderBookEntries = ordersRepository.getAggregatedOrders();
        Map<String, List<OrderBookEntryDto>> ordersByTicker = orderBookEntries.stream()
                .collect(Collectors.groupingBy(OrderBookEntryDto::getTicker));

        List<OrderBookResponseDto> orderBook = new ArrayList<>();
        for (Map.Entry<String, List<OrderBookEntryDto>> entry : ordersByTicker.entrySet()) {
            String ticker = entry.getKey();
            List<OrderBookEntryDto> orders = entry.getValue();

            List<OrderBookLevelDto> buySide = orders.stream()
                    .filter(o -> o.getSide().equals(Side.BUY))
                    .map(o -> new OrderBookLevelDto(o.getPrice(), o.getTotalQuantity()))
                    .sorted(Comparator.comparingLong(OrderBookLevelDto::getPrice).reversed())
                    .collect(Collectors.toList());

            List<OrderBookLevelDto> sellSide = orders.stream()
                    .filter(o -> o.getSide().equals(Side.SELL))
                    .map(o -> new OrderBookLevelDto(o.getPrice(), o.getTotalQuantity()))
                    .sorted(Comparator.comparingLong(OrderBookLevelDto::getPrice))
                    .collect(Collectors.toList());

            orderBook.add(new OrderBookResponseDto(ticker, buySide, sellSide));
        }

        return orderBook;
    }

    public void moveToHistory(UUID orderId) {
        ordersRepository.delete(orderId);
    }

    public void deleteStopOrder(UUID id) {
        stopOrdersRepository.delete(id);
    }

    public void insertStopOrder(StopOrderEntity stopOrder) {
        stopOrdersRepository.insert(stopOrder);
    }
}
