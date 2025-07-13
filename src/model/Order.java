package model;

import enums.OrderStatus;
import enums.OrderType;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static AtomicInteger counter = new AtomicInteger(0);
    private final String orderId;
    private OrderType orderType;
    private final Stock stock;
    private int quantity;
    private Double price;
    private OrderStatus orderStatus;
    private final  LocalDateTime orderTime;
    private final Investor investor;

    public Order(Investor investor, OrderType orderType, Stock stock, int quantity, Double price) {
        this.orderId = "ORD".concat(String.valueOf(counter.getAndIncrement()));
        this.investor = investor;
        this.orderType = orderType;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
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

    public Double getPrice() {
        return price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Investor getInvestor() {
        return investor;
    }

    public String getOrderId() {
        return orderId;
    }

    public boolean isBuyOrder() {
        return getOrderType().equals(OrderType.MARKET_BUY) || getOrderType().equals(OrderType.LIMIT_BUY);
    }

    public boolean isMarketOrder() {
        return getOrderType().equals(OrderType.MARKET_BUY) || getOrderType().equals(OrderType.MARKET_SELL);
    }
}
