package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Broker {

    private static volatile Broker broker;
    private static final List<StockExchange> exchanges = new ArrayList<>();
    private final String brokerId;
    private final String brokerName;

    private Broker(String brokerName) {
        this.brokerId = UUID.randomUUID().toString();
        this.brokerName = brokerName;
    }

    public static Broker getInstance(String brokerName) {
        if(broker == null) {
            synchronized (Broker.class) {
                if(broker == null) {
                    broker = new Broker(brokerName);
                }
            }
        }
        return broker;
    }

    public static void addExchanges(List<String> exchangesName) {
        for(String exchangeName : exchangesName) {
            exchanges.add(StockExchange.getInstance(exchangeName));
        }
    }

    public static void placeOrder(Investor investor, Order order, String exchange) throws Exception {
        StockExchange.getInstance(exchange).executeOrder(order, investor);
    }
}
