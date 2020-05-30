/*************************************                        
 * @author Tomás Raposo              *
 * Stock Market investor simulation  *
 *                                   *
 *             StockMEX              *
 *                                   *
 *************************************/

import java.util.*;
import java.util.stream.*;
import java.io.Serializable;

/**
 * Main stock Exchange class (text-based interface)
 */
public class StockExchange implements Serializable {

    private ArrayList<Company> listedCompanies;
    private ArrayList<Trader> listedTraders;
    private TradingEngine tradingEngine;
    private Trader trader;
    public StockExchange() {
	listedCompanies = new ArrayList<Company>();
	listedTraders = new ArrayList<Trader>();
	tradingEngine = new TradingEngine(this);
	trader = new Trader("user", 10000, 0.10);
    }
    public ArrayList<Company> getListedCompanies() {
	return listedCompanies;
    }
    public ArrayList<Trader> getListedTraders() {
	return listedTraders;
    }
    public TradingEngine getTradingEngine() {
	return tradingEngine;
    }
    public Trader getTrader() {
	return trader;
    }    
    /**
     * print out balance
     */
    public void balance(Trader trader) {
	System.out.println(trader.toString());
    }
    /**
     * print out active orders list
     */
    public void viewOrderList(Trader trader) {
	if (trader.getOrderList() != null) {
	    for (Order order : trader.getOrderList()) {
		System.out.printf("%n%s%n %s%n",
				  " Order ID: "+order.getOrderID(),
				  "Time stamp: "+order.getTimeStamp());
	    }
	} else {
	    System.out.println("Order list is currently empty.");
	}
    }
    /**
     * print out a given company's orderbook
     */
    public void viewPlacedOrders(String ticker) {
	for (Company company : listedCompanies) {
	    for (int i = 0; i < company.getCompanyStock().size(); i++) {
		Stock stock = company.getCompanyStock().get(i);
		if (stock.getTicker().equals(ticker)) {
		    OrderBook orderBook = company.getOrderBook();
		    System.out.println(orderBook.toString());
		}
	    }
	}
    }
    /**
     * (polymorphism explicitly) 
     * returns a string representation of a given company's stock
     */
    public String viewStockIndicators(String ticker) {
	for (Company company : listedCompanies) {
	    for (int i = 0; i < company.getCompanyStock().size(); i++) {
		Stock stock = company.getCompanyStock().get(i);
		if (stock.getTicker().equals(ticker)) {
		    Share issuedShare = stock.getShareType();
		    return issuedShare.toString();
		}
	    }
	}
	return new String("");
    }
    /**
     * peek at companies being traded.
     */
    public void peek() {
	for (Company company : listedCompanies) {
	    for (Stock stock : company.getCompanyStock()) {
	        String ticker = stock.getTicker();
		System.out.println(new StringBuilder('\n'+ticker)
				   .append("\n"+company.toString())
				   .append("\n"+stock.getShareType().toString())
				   .toString());
	    }
	}
    }
    /**
     * make price estimation.
     */
    public void estimate(int timePeriod) {
	Utils.getPriceEstimationOverTime(this.listedCompanies, timePeriod);	    
    }
    /**
     * Set all currently active orders both limit and market buys and sells.
     */
    void setActiveOrders() {
	for (Company company : listedCompanies) {
	    for (int i = 0; i < company.getCompanyStock().size(); i++) {
		Stock stock = company.getCompanyStock().get(i);
		OrderBook orderBook = company.getOrderBook();
		double currentPrice = company.getCurrentPrice();
		int availableVolume = company.getCompanyStockVolume();
		for (Trader trader : listedTraders) {
		    double price = Utils.randomPriceGenerator();
		    int quantity = new Random().nextInt(availableVolume) + 10;
		    if (new Random().nextDouble() > 0.5) {
			if (price < currentPrice) {
			    orderBook.addOrder(trader.placeBuyOrder(price,
								    quantity,
								    OrderType.LIMIT));
			} else {
			    orderBook.addOrder(trader.placeBuyOrder(currentPrice,
								    quantity,
								    OrderType.MARKET));
			}
		    } else {
			if (price > currentPrice) {
			    orderBook.addOrder(trader.placeSellOrder(price,
								     quantity,
								     OrderType.LIMIT));
			} else {
			    orderBook.addOrder(trader.placeSellOrder(currentPrice,
								     quantity,
								     OrderType.MARKET));
			}
		    }
		}
	    }
	}
    }
}

