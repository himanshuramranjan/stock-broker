package model;

import enums.OrderType;

public class Investor {
    private final String userId;
    private final Portfolio portfolio;
    private double totalBalance;
    private String userName;
    private String email;
    private Broker broker;

    public Investor(String userId, double totalBalance, String userName, String email, Broker broker) {
        this.userId = userId;
        this.portfolio = new Portfolio(userId);
        this.totalBalance = totalBalance;
        this.userName = userName;
        this.email = email;
        this.broker = broker;
    }

    public void updateBalance(double balance) {
        this.totalBalance += balance;
    }

    public void placeOrder(OrderType type, Stock stock, int quantity, double price, String exchange) throws Exception {
        Order order = new Order(type, stock, quantity, price);
        broker.placeOrder(this, order, exchange);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Broker getBroker() {
        return broker;
    }
}
