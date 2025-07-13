package service;

import enums.OrderType;
import model.Investor;
import model.Order;
import model.OrderBook;
import model.Trade;

import java.util.PriorityQueue;
import java.util.function.BiPredicate;

public class TradeMatchingEngine {
    private static final TradeMatchingEngine tradeMatchingEngine = new TradeMatchingEngine();

    private TradeMatchingEngine() {}

    public static TradeMatchingEngine getInstance() { return tradeMatchingEngine; }
    public synchronized Trade findMatch(Order incomingOrder, Investor investor, OrderBook orderBook) {
        PriorityQueue<Order> oppositeOrders = getOppositeOrders(incomingOrder, orderBook);
        BiPredicate<Order, Order> priceMatcher = getPriceMatcherForOrderType(incomingOrder.getOrderType());

        return findExactQuantityMatch(incomingOrder, investor, oppositeOrders, priceMatcher);
    }

    private Trade findExactQuantityMatch(Order incomingOrder, Investor investor, PriorityQueue<Order> oppositeOrders, BiPredicate<Order, Order> priceMatcher) {
        while(!oppositeOrders.isEmpty()) {
            Order oppositeOrder = oppositeOrders.peek();

            // Price condition not met, stop looking
            if(!priceMatcher.test(incomingOrder, oppositeOrder)) {
                break;
            }

            // not performing multi-leg trade (a single trade should be matched with another single leg)
            if(oppositeOrder.getQuantity() == incomingOrder.getQuantity()) {

                // in case of market buy/sell price is set as best available
                if(incomingOrder.isMarketOrder()) {
                    incomingOrder.setPrice(oppositeOrder.getPrice());
                }

                // remove matched order
                oppositeOrders.poll();
                return buildTrade(oppositeOrder, incomingOrder, investor);
            }
        }
        return null;
    }

    private PriorityQueue<Order> getOppositeOrders(Order incomingOrder, OrderBook orderBook) {
        if(incomingOrder.isBuyOrder()) {
            return orderBook.getSellOrder();
        }
        return orderBook.getBuyOrder();
    }

    private BiPredicate<Order, Order> getPriceMatcherForOrderType(OrderType orderType) {
        return switch (orderType) {
            case LIMIT_BUY -> (incomingOrder, bookOrder) -> bookOrder.getPrice() <= incomingOrder.getPrice();
            case LIMIT_SELL -> (incomingOrder, bookOrder) -> bookOrder.getPrice() >= incomingOrder.getPrice();
            case MARKET_BUY, MARKET_SELL -> (incomingOrder, bookOrder) -> true;
        };
    }

    private Trade buildTrade(Order matchedOrder, Order incomingOrder, Investor incomingInvestor) {
        Investor buyer, seller;
        Order buyOrder, sellOrder;

        if (incomingOrder.isBuyOrder()) {
            buyer = incomingInvestor;
            seller = matchedOrder.getInvestor();
            buyOrder = incomingOrder;
            sellOrder = matchedOrder;
        } else {
            seller = incomingInvestor;
            buyer = matchedOrder.getInvestor();
            sellOrder = incomingOrder;
            buyOrder = matchedOrder;
        }
        return new Trade(buyOrder, sellOrder, buyer, seller);
    }
}
