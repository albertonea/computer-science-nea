package com.eastbarnetschool.ordermatchingengine.domain;
import java.util.concurrent.*;

public class OrderQueue {
    private final OrderBook orderBook;
    private final String ticker;
    private final BlockingQueue<Pair<Order, BlockingQueue<MatchingEngineResponse>>> orderQueue;
    private volatile boolean running;

    public OrderQueue(String ticker) {
        this.ticker = ticker;
        this.orderBook = new OrderBook();
        this.orderQueue = new LinkedBlockingQueue<>();
        this.running = true;
        startOrderProcessor();
    }

    public MatchingEngineResponse placeOrder(Order order) {
        BlockingQueue<MatchingEngineResponse> responseQueue = new ArrayBlockingQueue<>(1);
        try {
            orderQueue.put(new Pair<>(order, responseQueue));
            return responseQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Order placement interrupted", e);
        }
    }

    private void startOrderProcessor() {
        Thread processorThread = new Thread(() -> {
            while (running || !orderQueue.isEmpty()) {
                try {
                    Pair<Order, BlockingQueue<MatchingEngineResponse>> task = orderQueue.take();
                    Order order = task.getKey();
                    BlockingQueue<MatchingEngineResponse> responseQueue = task.getValue();

                    MatchingEngineResponse response = processOrder(order);
                    responseQueue.put(response);
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
            return orderBook.placeLimitOrder(order);
        } else if (order.getOrderType() == OrderType.MARKET) {
            return orderBook.placeMarketOrder(order);
        } else {
            throw new IllegalArgumentException("Unsupported order type: " + order.getOrderType());
        }
    }

    public void stop() {
        running = false;
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
