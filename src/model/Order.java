package model;

import enums.OrderStatus;
import enums.OrderType;

import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static AtomicInteger counter = new AtomicInteger(0);
    private final String orderId;
    private OrderType orderType;
    private Stock stock;
    private int quantity;
    private double price;
    private OrderStatus orderStatus;

    public Order(OrderType orderType, Stock stock, int quantity, double price) {
        this.orderId = "ORD".concat(String.valueOf(counter.getAndIncrement()));
        this.orderType = orderType;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = OrderStatus.PENDING;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
