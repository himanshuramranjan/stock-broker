import enums.OrderType;
import model.Broker;
import model.Investor;
import model.Stock;
import model.StockExchange;

public class Main {
    public static void main(String[] args) throws Exception {

        Broker groww = Broker.getInstance("Groww");
        StockExchange nse = StockExchange.getInstance("NSE");
        StockExchange bse = StockExchange.getInstance("BSE");

        // Add Stocks to Exchange
        Stock tcs = new Stock("TCS", "Tata Consultancy Services", 3500.00);
        nse.addStock(tcs, 10000);
        bse.addStock(tcs, 10000);

        // Create Investor and Add Initial Balance
        Investor investor = new Investor("U1001", 50000, "John", "john@example.com", groww);

        // BUY Order: Investor Buys 10 TCS Shares
        investor.placeOrder(OrderType.MARKET_BUY, tcs, 10, tcs.getCurrentPrice(), "NSE");

        // SELL Order: Investor Tries to Sell 5 TCS Shares
        investor.placeOrder(OrderType.MARKET_SELL, tcs, 5, tcs.getCurrentPrice(), "NSE");
    }
}