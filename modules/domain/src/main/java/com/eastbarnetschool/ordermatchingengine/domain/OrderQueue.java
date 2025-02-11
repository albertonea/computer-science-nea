package com.eastbarnetschool.ordermatchingengine.domain;
import com.eastbarnetschool.ordermatchingengine.domain.listeners.PriceUpdateListener;
import com.eastbarnetschool.ordermatchingengine.domain.orders.LimitOrder;
import com.eastbarnetschool.ordermatchingengine.domain.orders.MarketOrder;
import com.eastbarnetschool.ordermatchingengine.domain.orders.Order;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class OrderQueue implements PriceUpdateListener {
    private final OrderBook orderBook;
    private final String ticker;
    private final BlockingQueue<Pair<Order, CompletableFuture<MatchingEngineResponse>>> orderQueue;
    private volatile boolean running;
    private final PriorityQueue<StopOrder> stopBuyOrders;
    private final PriorityQueue<StopOrder> stopSellOrders;

    public OrderQueue(String ticker) {
        this.ticker = ticker;
        this.orderBook = new OrderBook(this);
        this.orderQueue = new LinkedBlockingQueue<>();
        this.running = true;
        this.stopSellOrders = new PriorityQueue<>(Comparator.comparing(StopOrder::getExecutionPrice).reversed());
        this.stopBuyOrders = new PriorityQueue<>(Comparator.comparing(StopOrder::getExecutionPrice));
        startOrderProcessor();
    }

    public void updateStopOrders() {
        while (!stopSellOrders.isEmpty()) {
            StopOrder stopOrder = stopSellOrders.peek();
            Order order = stopOrder.getOrder();
            if (stopOrder.getExecutionPrice() >= orderBook.getLastPrice()) {
                stopSellOrders.poll();
                placeOrder(order);
            } else {
                break;
            }
        }

        while (!stopBuyOrders.isEmpty()) {
            StopOrder stopOrder = stopBuyOrders.peek();
            Order order = stopOrder.getOrder();
            if (stopOrder.getExecutionPrice() <= orderBook.getLastPrice()) {
                stopBuyOrders.poll();
                placeOrder(order);
            } else {
                break;
            }
        }
    }

    public void placeStopOrder(StopOrder stopOrder) {
        if (stopOrder.getOrder().getSide() == Side.BUY) {
            if (stopOrder.getExecutionPrice() <= orderBook.getLastPrice()) {
                placeOrder(stopOrder.getOrder());
            } else {
                stopBuyOrders.add(stopOrder);
            }
        } else {
            if (stopOrder.getExecutionPrice() >= orderBook.getLastPrice()) {
                placeOrder(stopOrder.getOrder());
            } else {
                stopSellOrders.add(stopOrder);
            }
        }
    }

    public CompletableFuture<MatchingEngineResponse> placeOrder(Order order) {
        CompletableFuture<MatchingEngineResponse> future = new CompletableFuture<>();
        orderQueue.offer(new Pair<>(order, future));
        return future;
    }

    private void startOrderProcessor() {
        Thread processorThread = new Thread(() -> {
            while (running || !orderQueue.isEmpty()) {
                try {
                    Pair<Order, CompletableFuture<MatchingEngineResponse>> task = orderQueue.take();
                    Order order = task.getKey();
                    CompletableFuture<MatchingEngineResponse> future = task.getValue();

                    MatchingEngineResponse response = processOrder(order);

                    future.complete(response);
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

    private MatchingEngineResponse processOrder(Order order) {
        if (order.getOrderType() == OrderType.LIMIT) {
            return orderBook.placeLimitOrder((LimitOrder) order);
        } else if (order.getOrderType() == OrderType.MARKET) {
            return orderBook.placeMarketOrder((MarketOrder) order);
        } else {
            throw new IllegalArgumentException("Unsupported order type: " + order.getOrderType());
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void onPriceUpdated(Long newPrice) {
        updateStopOrders();
    }

    public static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
