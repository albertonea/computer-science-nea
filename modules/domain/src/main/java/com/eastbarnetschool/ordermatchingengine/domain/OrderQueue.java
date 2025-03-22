package com.eastbarnetschool.ordermatchingengine.domain;
import com.eastbarnetschool.ordermatchingengine.domain.events.OrderPlacedEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.StopOrderTriggeredEvent;
import com.eastbarnetschool.ordermatchingengine.domain.events.StopOrderQueuedEvent;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.PriceUpdateListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

import java.util.Comparator;
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
        // stop sell order queue with priority to the lowest price
        this.stopSellOrders = new PriorityQueue<>(PriorityQueueOrder.ASC);
        // stop buy order queue with priority to the highest price
        this.stopBuyOrders = new PriorityQueue<>(PriorityQueueOrder.DESC);
        startOrderProcessor();
    }

    // checks if the stop orders can be triggered
    public void updateStopOrders() {
        // loop through sell side until it hits the first order
        // that cant be triggered then break
        while (!stopSellOrders.isEmpty()) {
            StopOrder stopOrder = stopSellOrders.peek();
            Order order = stopOrder.getOrder();
            if (stopOrder.getTriggerPrice() >= orderBook.getLastPrice()) {
                eventPublisher.publishStopOrderTriggeredEvent(new StopOrderTriggeredEvent(stopOrder));
                stopSellOrders.dequeue();
                placeOrder(order);
            } else {
                break;
            }
        }

        // same loop for buy side
        while (!stopBuyOrders.isEmpty()) {
            StopOrder stopOrder = stopBuyOrders.peek();
            Order order = stopOrder.getOrder();
            if (stopOrder.getTriggerPrice() <= orderBook.getLastPrice()) {
                eventPublisher.publishStopOrderTriggeredEvent(new StopOrderTriggeredEvent(stopOrder));
                stopBuyOrders.dequeue();
                placeOrder(order);
            } else {
                break;
            }
        }
    }

    // function to place a stop order
    public void placeStopOrder(StopOrder stopOrder) {
        // check the side of the order, send an order queued event
        // and queue the order
        if (stopOrder.getOrder().getSide() == Side.BUY) {
            eventPublisher.publishStopOrderQueuedEvent(new StopOrderQueuedEvent(stopOrder));
            stopBuyOrders.enqueue(stopOrder, stopOrder.getOrder().getPrice().intValue());
        } else {
            eventPublisher.publishStopOrderQueuedEvent(new StopOrderQueuedEvent(stopOrder));
            stopSellOrders.enqueue(stopOrder, stopOrder.getOrder().getPrice().intValue());
        }

        // call update to check if order can be triggered immediately
        updateStopOrders();
    }

    // function to place a limit or market order
    public void placeOrder(Order order) {
        // place order in queue
        orderQueue.offer(order);
    }

    // function to process orders on the queue
    private void startOrderProcessor() {
        // initialise a thread to ingest orders on the queue
        Thread processorThread = new Thread(() -> {
            while (running || !orderQueue.isEmpty()) {
                try {
                    // take the next order from the queue and call process
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

        // start the thread
        processorThread.setDaemon(true);
        processorThread.start();
    }

    // function to place the order onto the orderbook
    private void processOrder(Order order) {
        // send order placed event
        eventPublisher.publishOrderPlacedEvent(new OrderPlacedEvent(order));
        // place order on orderbook
        orderBook.placeOrder(order);
    }

    public void stop() {
        running = false;
    }

    // callback function whenever the price updates in the orderbook
    @Override
    public void onPriceUpdated(Long newPrice) {
        // call update stop orders to check if any can be triggered
        updateStopOrders();
    }
}
