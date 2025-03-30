package model;

import enums.OrderType;
import exception.InsufficientStockException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockExchange {
    private static final Map<String, StockExchange> exchanges = new ConcurrentHashMap<>();
    private String exchangeName;
    private final Map<Stock, Integer> listedStocks = new ConcurrentHashMap<>();

    private StockExchange(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public static StockExchange getInstance(String exchangeName) {
        return exchanges.computeIfAbsent(exchangeName, key -> new StockExchange(key));
    }

    public synchronized void executeOrder(Order order, Investor investor) throws Exception {
        int stockAvailability = listedStocks.getOrDefault(order.getStock(), 0);
        if ((order.getOrderType().equals(OrderType.LIMIT_BUY) || order.getOrderType().equals(OrderType.MARKET_BUY)) && stockAvailability <= order.getQuantity()) {
            throw new InsufficientStockException("Stock not available on :" + exchangeName);
        }

        OrderProcessor.getInstance().processOrder(order, investor);
        listedStocks.put(order.getStock(), listedStocks.get(order.getStock()) - order.getQuantity());
    }

    public void addStock(Stock stock, int quantity) {
        listedStocks.put(stock, listedStocks.getOrDefault(stock, 0) + quantity);
    }

    public void displayAllStock() {
        for(Stock stock : listedStocks.keySet()) {
            System.out.println(stock + " : " + listedStocks.get(stock));
        }
    }

    public void updateStockPrice(Stock stock, double newPrice) {
        if (listedStocks.containsKey(stock)) {
            stock.setCurrentPrice(newPrice);
            System.out.println("Updated " + stock.getSymbol() + " price to " + newPrice);
        }
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
