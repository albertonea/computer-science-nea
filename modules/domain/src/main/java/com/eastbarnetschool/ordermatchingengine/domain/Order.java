package com.eastbarnetschool.ordermatchingengine.domain;

public class Order {
    private Long price;
    private Integer quantity;
    private String ticker;
    private Side side;
    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getTicker() {
        return ticker;
    }

    public Side getSide() {
        return side;
    }

    public void fill(Integer quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        }
    }
}
