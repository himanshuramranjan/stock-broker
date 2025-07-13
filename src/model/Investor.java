package model;

import enums.OrderType;

public class Investor {
    private final String userId;
    private final Portfolio portfolio;
    private double totalBalance;
    private String userName;
    private String email;

    public Investor(String userId, double totalBalance, String userName, String email) {
        this.userId = userId;
        this.portfolio = new Portfolio(userId);
        this.totalBalance = totalBalance;
        this.userName = userName;
        this.email = email;
    }

    public void updateBalance(double balance) {
        this.totalBalance += balance;
    }

    public void placeOrder(OrderType type, Stock stock, int quantity, Double price, String exchange) {
        Order order = new Order(this, type, stock, quantity, price);
        Broker.getInstance().placeOrder(this, order, exchange);
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
}
