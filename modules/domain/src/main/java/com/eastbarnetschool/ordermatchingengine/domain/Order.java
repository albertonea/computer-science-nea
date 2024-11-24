package com.eastbarnetschool.ordermatchingengine.domain;

public class Order {
    private Long price;
    private Integer initialQuantity;
    private Integer remainingQuantity;
    private String ticker;
    private Side side;
    private OrderType orderType;

    public Order(Long price, Integer quantity, String ticker, Side side, OrderType orderType) {
        this.price = price;
        this.initialQuantity = quantity;
        this.remainingQuantity = quantity;
        this.ticker = ticker;
        this.side = side;
        this.orderType = orderType;
    }

    public Long getPrice() {
        return price;
    }

    public String getTicker() {
        return ticker;
    }

    public Side getSide() {
        return side;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public Integer fill(Integer fillQuantity) {
        if (fillQuantity > remainingQuantity) {
            throw new IllegalArgumentException("Fill quantity exceeds remaining quantity");
        }
        remainingQuantity -= fillQuantity;

        return remainingQuantity;
    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
