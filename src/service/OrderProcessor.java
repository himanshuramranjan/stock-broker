package model;

import enums.OrderStatus;
import exception.InsufficientFundsException;
import exception.InsufficientStockException;
import service.TradeMatchingEngine;

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
        OrderBook orderBook = OrderBook.getOrderBook(order.getStock());
        Trade trade = TradeMatchingEngine.getInstance().findMatch(order, investor, orderBook);

        try {
            executeTrade(trade);
            System.out.println("Your trade has been processed successfully");
        } catch (Exception e) {
            System.out.println("Exception occurred while executing the trade " + e.getMessage());
            orderBook.addOrder(trade.getBuyOrder());
            orderBook.addOrder(trade.getSellOrder());
        }
    }

    private void executeTrade(Trade trade) throws InsufficientStockException, InsufficientFundsException {

        if(trade == null) throw new RuntimeException("Internal error, Trade not found");

        Order buyOrder = trade.getBuyOrder();
        Order sellOrder = trade.getSellOrder();
        Investor buyer = trade.getBuyer();
        Investor seller = trade.getSeller();

        double totalCost = buyOrder.getPrice() * buyOrder.getQuantity();
        Stock stock = buyOrder.getStock();
        int quantity = buyOrder.getQuantity();

        validateFund(buyer, totalCost, buyOrder);
        validateStock(seller, stock, quantity, sellOrder);

        buyer.updateBalance(-totalCost);
        buyer.getPortfolio().addStock(stock, quantity);

        seller.updateBalance(totalCost);

        buyOrder.setOrderStatus(OrderStatus.EXECUTED);
        sellOrder.setOrderStatus(OrderStatus.EXECUTED);

        System.out.println("Trade Executed: " + buyOrder.getOrderId() + " <-> " + sellOrder.getOrderId());
    }

    private static void validateStock(Investor seller, Stock stock, int quantity, Order sellOrder) throws InsufficientStockException {
        if (!seller.getPortfolio().removeStock(stock, quantity)) {
            sellOrder.setOrderStatus(OrderStatus.INSUFFICIENT_STOCK);
            throw new InsufficientStockException("Seller " + seller.getUserName() + " doesn't have enough stock");
        }
    }

    private static void validateFund(Investor buyer, double totalCost, Order buyOrder) throws InsufficientFundsException {
        if (buyer.getTotalBalance() < totalCost) {
            buyOrder.setOrderStatus(OrderStatus.INSUFFICIENT_FUND);
            throw new InsufficientFundsException("Insufficient funds for investor: " + buyer.getUserName());
        }
    }
}
