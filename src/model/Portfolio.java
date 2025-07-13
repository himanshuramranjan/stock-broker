package model;

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

    public synchronized boolean removeStock(Stock stock, int quantity) {
        if(holdings.getOrDefault(stock, 0) >= quantity) {
            holdings.put(stock, holdings.get(stock) - quantity);
            if(holdings.get(stock) == 0) holdings.remove(stock);
            return true;
        } else {
            return false;
        }
    }

    public void displayPortfolio() {
        for(Stock stock : holdings.keySet()) {
            System.out.println(stock.getSymbol() + " " + holdings.get(stock));
        }
    }

}
