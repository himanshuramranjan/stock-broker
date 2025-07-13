package service;

import enums.OrderStatus;
import exception.InsufficientFundsException;
import exception.InsufficientStockException;
import exception.NoTradeFoundException;
import model.*;

public class OrderProcessor {
    private static OrderProcessor orderProcessor = new OrderProcessor();
    private OrderProcessor() {}

    public static OrderProcessor getInstance() {
        return orderProcessor;
    }

    public void processOrder(Order order, Investor investor) {
        OrderBook orderBook = OrderBook.getOrderBook(order.getStock());
        Trade trade = TradeMatchingEngine.getInstance().findMatch(order, investor, orderBook);

        try {
            executeTrade(trade);
            System.out.println("Your trade has been processed successfully");
        } catch(NoTradeFoundException e) {
            System.out.println("Exception occurred while executing the trade " + e.getMessage());
            orderBook.addOrder(order);
        } catch(Exception e) {
            System.out.println("Exception occurred while executing the trade " + e.getMessage());
            orderBook.addOrder(trade.buyOrder());
            orderBook.addOrder(trade.sellOrder());
        }
    }

    private void executeTrade(Trade trade) throws InsufficientStockException, InsufficientFundsException, NoTradeFoundException {

        if(trade == null) throw new NoTradeFoundException("No buyer or seller present at this moment for the trade, your order is added to the queue");

        Order buyOrder = trade.buyOrder();
        Order sellOrder = trade.sellOrder();
        Investor buyer = trade.buyer();
        Investor seller = trade.seller();

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
