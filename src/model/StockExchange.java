package model;

import service.OrderProcessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StockExchange {
    private static final Map<String, StockExchange> exchanges = new ConcurrentHashMap<>();
    private String exchangeName;
    private final Set<Stock> listedStocks = new HashSet<>();

    private StockExchange(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public static StockExchange getInstance(String exchangeName) {
        return exchanges.computeIfAbsent(exchangeName, StockExchange::new);
    }

    public synchronized void executeOrder(Order order, Investor investor) throws IllegalArgumentException {

        if (!listedStocks.contains(order.getStock())) {
            throw new IllegalArgumentException("Stock not available on :" + exchangeName);
        }
        OrderProcessor.getInstance().processOrder(order, investor);
    }

    public void addStock(Stock stock) {
        listedStocks.add(stock);
    }

    public void displayAllStock() {
        for(Stock stock : listedStocks) {
            System.out.println(stock + " : is currently listed ");
        }
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
