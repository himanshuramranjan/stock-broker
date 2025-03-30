package model;

import enums.OrderStatus;
import exception.InsufficientFundsException;
import exception.InsufficientStockException;

public class OrderProcessor {
    private static volatile OrderProcessor orderProcessor;
    private OrderProcessor() {}

    public static OrderProcessor getInstance() {
        if(orderProcessor == null) {
            synchronized (OrderProcessor.class) {
                if(orderProcessor == null) {
                    orderProcessor = new OrderProcessor();
                }
            }
        }
        return orderProcessor;
    }

    public void processOrder(Order order, Investor investor) {
        try {
            switch(order.getOrderType()) {
                case MARKET_BUY, LIMIT_BUY :
                    processBuyOrder(order, investor);
                    break;
                case LIMIT_SELL, MARKET_SELL:
                    processSellOrder(order, investor);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error in processing the order " + e.getMessage());
        }
    }

    private synchronized void processBuyOrder(Order order, Investor investor) throws InsufficientFundsException {
        double requiredAmount = order.getStock().getCurrentPrice() * order.getQuantity();
        if(requiredAmount > investor.getTotalBalance()) {
            throw new InsufficientFundsException("Insufficient balance for given quantity");
        }
        investor.updateBalance(-requiredAmount);
        investor.getPortfolio().addStock(order.getStock(), order.getQuantity());
        order.setOrderStatus(OrderStatus.EXECUTED);
    }

    private void processSellOrder(Order order, Investor investor) throws InsufficientStockException {
        investor.getPortfolio().removeStock(order.getStock(), order.getQuantity(), order.getPrice());
        investor.updateBalance(order.getQuantity() * order.getPrice());
        order.setOrderStatus(OrderStatus.EXECUTED);
    }
}
