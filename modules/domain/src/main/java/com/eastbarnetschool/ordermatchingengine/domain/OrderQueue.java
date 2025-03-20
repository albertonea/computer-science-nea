package com.eastbarnetschool.ordermatchingengine.domain;
import com.eastbarnetschool.ordermatchingengine.domain.events.OrderPlacedEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.StopOrderTriggeredEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.StopOrderQueuedEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.StopOrderTriggeredEvent;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.PriceUpdateListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class OrderQueue implements PriceUpdateListener {
    private final OrderBook orderBook;
    private final String ticker;
    private final BlockingQueue<Order> orderQueue;
    private volatile boolean running;
    private final PriorityQueue<StopOrder> stopBuyOrders;
    private final PriorityQueue<StopOrder> stopSellOrders;
    private final OrderGateway eventPublisher;

    public OrderQueue(String ticker, OrderGateway eventPublisher) {
        this.ticker = ticker;
        this.eventPublisher = eventPublisher;
        this.orderBook = new OrderBook(this, eventPublisher);
        this.orderQueue = new LinkedBlockingQueue<>();
        this.running = true;
        this.stopSellOrders = new PriorityQueue<>(Comparator.comparing(StopOrder::getTriggerPrice).reversed());
        this.stopBuyOrders = new PriorityQueue<>(Comparator.comparing(StopOrder::getTriggerPrice));
        startOrderProcessor();
    }

    public void updateStopOrders() {
        while (!stopSellOrders.isEmpty()) {
            StopOrder stopOrder = stopSellOrders.peek();
            Order order = stopOrder.getOrder();
            if (stopOrder.getTriggerPrice() >= orderBook.getLastPrice()) {
                eventPublisher.publishStopOrderTriggeredEvent(new StopOrderTriggeredEvent(stopOrder));
                stopSellOrders.poll();
                placeOrder(order);
            } else {
                break;
            }
        }

        while (!stopBuyOrders.isEmpty()) {
            StopOrder stopOrder = stopBuyOrders.peek();
            Order order = stopOrder.getOrder();
            if (stopOrder.getTriggerPrice() <= orderBook.getLastPrice()) {
                eventPublisher.publishStopOrderTriggeredEvent(new StopOrderTriggeredEvent(stopOrder));
                stopBuyOrders.poll();
                placeOrder(order);
            } else {
                break;
            }
        }
    }

    public void placeStopOrder(StopOrder stopOrder) {
        if (stopOrder.getOrder().getSide() == Side.BUY) {
            eventPublisher.publishStopOrderQueuedEvent(new StopOrderQueuedEvent(stopOrder));
            stopBuyOrders.add(stopOrder);
        } else {
            eventPublisher.publishStopOrderQueuedEvent(new StopOrderQueuedEvent(stopOrder));
            stopSellOrders.add(stopOrder);
        }
        updateStopOrders();
    }

    public void placeOrder(Order order) {
        orderQueue.offer(order);
    }

    private void startOrderProcessor() {
        Thread processorThread = new Thread(() -> {
            while (running || !orderQueue.isEmpty()) {
                try {
                    Order order = orderQueue.take();
                    processOrder(order);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Order processor interrupted");
                    break;
                } catch (Exception ex) {
                    System.err.println("Order processing failed: " + ex.getMessage());
                }
            }
        });
        processorThread.setDaemon(true);
        processorThread.start();
    }

    private void processOrder(Order order) {
        eventPublisher.publishOrderPlacedEvent(new OrderPlacedEvent(order));
        orderBook.placeOrder(order);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void onPriceUpdated(Long newPrice) {
        updateStopOrders();
    }
}
