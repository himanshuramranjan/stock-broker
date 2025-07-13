import enums.OrderType;
import model.*;

public class StockBrokerApplication {

    public static void main(String[] args) {
        // Step 1: Setup Exchange and Stock
        Stock apple = new Stock("AAPL", "Apple Inc.", 180.00);
        StockExchange nse = StockExchange.getInstance("NSE");
        nse.addStock(apple); // Listed on exchange

        // Step 3: Create 2 investors
        Investor seller = new Investor("INV01", 1000.0, "Anmol", "anmol@example.com");
        seller.getPortfolio().addStock(apple, 10);

        Investor buyer = new Investor("INV02", 2000.0, "Himanshu", "himanshu@example.com");

        Broker.getInstance().addInvestor(seller);
        Broker.getInstance().addInvestor(buyer);

        // --------- SCENARIO 1: SELLER places LIMIT_SELL, BUYER places LIMIT_BUY ---------

        System.out.println("\n--- Scenario 1: Matching LIMIT_BUY vs LIMIT_SELL ---");

        // Step 4: LIMIT_SELL for 5 shares @ ₹175
        seller.placeOrder(OrderType.LIMIT_SELL, apple, 5, 175.0, "NSE");

        // Step 5: LIMIT_BUY for 5 shares @ ₹180 (matches seller's ask)
        buyer.placeOrder(OrderType.LIMIT_BUY, apple, 5, 180.0, "NSE");

        // Step 6: View balances and portfolios
        printInvestorSummary(seller);
        printInvestorSummary(buyer);

        // --------- SCENARIO 2: BUYER places LIMIT_BUY first, SELLER places MARKET_SELL ---------

        System.out.println("\n--- Scenario 2: Matching MARKET_SELL vs existing LIMIT_BUY ---");

        // Step 7: LIMIT_BUY for 5 shares @ ₹170 (goes into book)
        buyer.placeOrder(OrderType.LIMIT_BUY, apple, 5, 170.0, "NSE");

        // Step 8: MARKET_SELL for 5 shares (should match Himanshu's LIMIT_BUY)
        seller.placeOrder(OrderType.MARKET_SELL, apple, 5, null, "NSE");

        // Step 9: View final state
        printInvestorSummary(seller);
        printInvestorSummary(buyer);
    }

    private static void printInvestorSummary(Investor investor) {
        System.out.println("\nInvestor: " + investor.getUserName());
        System.out.println("Balance: ₹" + investor.getTotalBalance());
        System.out.println("Portfolio:");
        investor.getPortfolio().displayPortfolio();
    }
}
