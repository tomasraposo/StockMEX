import java.util.HashMap;
import java.util.*;
import java.util.Timer.*;
//import javax.swing.Timer;
import java.util.TimerTask.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.io.Serializable;
/**
 * TradingEngine class.
 * Class for matching all placed orders
 * across all listed companies in the
 * stock exchange.
 */
public final class TradingEngine implements Serializable {

    private StockExchange stockExchange;
    private HashMap<Company, OrderBook> orderBooks;

    public TradingEngine(StockExchange stockExchange) {
	this.stockExchange = stockExchange;
    }
    /**
     * Hashmap for mapping a given company to its corresponding
     * orderbook.
     */
    public void orderMatching() {
	HashMap<Company, OrderBook> orderBooks = new HashMap<Company, OrderBook>(){{
		for (Company company : stockExchange.getListedCompanies()) {
		    OrderBook companyOrderBook = company.getOrderBook();
		    this.put(company,  companyOrderBook);
		}
	    }};
	this.orderBooks = orderBooks;
    }
    /*
     * executes all market and limit orders of 
     * a given listed company.
     */
    public void execute(Company company) {
	System.out.println(new Date() + " | Executing all placed buy and sell orders.");
	OrderBook orderBook = company.getOrderBook();
	orderBook.executeMarketOrders();
	orderBook.executeLimitOrders();
	//System.out.println(orderBook.toString() + "\r");
	//orderBook.executeLimitOrders();
	System.out.println(new Date() + " | There are no more buy/sell orders to match.");
    }
}
