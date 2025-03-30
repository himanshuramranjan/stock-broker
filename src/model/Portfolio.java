package model;

import exception.InsufficientStockException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Portfolio {
    private final String investorId;
    private final Map<Stock, Integer> holdings;

    public Portfolio(String investorId) {
        this.investorId = investorId;
        this.holdings = new ConcurrentHashMap<>();
    }

    public synchronized void addStock(Stock stock, int quantity) {
        holdings.put(stock, holdings.getOrDefault(stock, 0) + quantity);
    }

    public synchronized void removeStock(Stock stock, int quantity, double price) throws InsufficientStockException {
        if(holdings.getOrDefault(stock, 0) >= quantity && stock.getCurrentPrice() >= price) {
            holdings.put(stock, holdings.get(stock) - quantity);
            if(holdings.get(stock) == 0) holdings.remove(stock);
        } else {
            throw new InsufficientStockException("Not enough stocks to sell or price is not enough");
        }
    }

    public void displayPortfolio() {
        for(Stock stock : holdings.keySet()) {
            System.out.println(stock.getSymbol() + " " + holdings.get(stock));
        }
    }

}
