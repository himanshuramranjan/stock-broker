package model;

import java.util.ArrayList;
import java.util.List;

public class Broker {
    private static final Broker broker = new Broker();
    private final String brokerName = "ZERODHA";
    private final List<StockExchange> exchanges = new ArrayList<>();
    private final List<Investor> investors = new ArrayList<>();

    private Broker() {}

    public static Broker getInstance() {
        return broker;
    }

    public void addExchange(StockExchange exchange) {
        exchanges.add(exchange);
    }

    public void addInvestor(Investor investor) {
        investors.add(investor);
    }

    public void placeOrder(Investor investor, Order order, String exchange) {
        StockExchange.getInstance(exchange).executeOrder(order, investor);
    }

    public String getBrokerName() {
        return brokerName;
    }

    public List<Investor> getInvestors() {
        return investors;
    }
}
