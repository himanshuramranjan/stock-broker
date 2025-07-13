package model;

import enums.OrderType;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBook {

    private static final Map<String, OrderBook> stockOrderBook = new ConcurrentHashMap<>();
    private final PriorityQueue<Order> buyOrder;
    private final PriorityQueue<Order> sellOrder;

    private OrderBook() {
        // sorted based on price and then time order was placed
        buyOrder = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).reversed().thenComparing(Order::getOrderTime));
        sellOrder = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice).thenComparing(Order::getOrderTime));
    }

    public PriorityQueue<Order> getBuyOrder() {
        return buyOrder;
    }

    public PriorityQueue<Order> getSellOrder() {
        return sellOrder;
    }

    public static OrderBook getOrderBook(Stock stock) {
        return stockOrderBook.computeIfAbsent(stock.getSymbol(), k -> new OrderBook());
    }

    public void addOrder(Order order) {
        if(order.isBuyOrder()) {
            buyOrder.offer(order);
        } else {
            sellOrder.offer(order);
        }
    }
}
