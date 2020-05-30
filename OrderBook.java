import java.util.*;
import java.util.stream.*;
import java.util.stream.Stream;
import java.time.LocalDateTime; 
import java.io.Serializable;

/**
 * OrderBook class
 * This class deal with all placed orders.
 * Each company has one.
 * It keeps the logs of all active placed orders.
 * It has two priority queues: 
 * 
 * 1) for the buy orders
 * 1) for the sell orders.
 *
 * Each priority queue handles the actual 
 * sorting of the data, i.e., orders, 
 * according to its price.
 *
 * The orderbook handles the processing,
 * matching and execution of all active orders. 
 */

public class OrderBook implements Serializable {
    
    private int volume;
    PriorityQueue<Order> bids, asks;
    
    public OrderBook() {
	volume = 0;
	bids = new PriorityQueue<Order>(10000, new OrderComparator());
	asks = new PriorityQueue<Order>(10000, new OrderComparator());
    }
    /**
     * Set of methods for interacting with the order book
     */
    public void clear() {
	volume = 0;
	bids.clear();
	asks.clear();
    }
    public PriorityQueue<Order> getBids() {
	return bids;
    }    
    public PriorityQueue<Order> getAsks() {
	return asks;
    }
    public void addOrder(Order order) {
        String quote = order.getQuoteType();
	if (quote.equals("bid")) 
	    bids.insert(order);	 
	else if (quote.equals("ask"))
	    asks.insert(order);
    }
    public void removeOrder(Order order) {
	if (order != null) {
	    String quote = order.getQuoteType();
	    if (quote.equals("bid")
		&& matches(this.bids, order)) {
		try {bids.remove(order);}
		catch (NoSuchElementException e) {
		    System.out.println("No buy orders have been placed.");
		}
	    } else if (quote.equals("ask")
		       && matches(this.asks, order)) {
		try {asks.remove(order);}
		catch (NoSuchElementException e) {
		    System.out.println("No sell orders have been placed.");
		}
	    }
	}
    }
    public void updateOrder(Order order) {
	OrderType orderType = order.getOrderType();
	OrderStatus orderStatus = order.getOrderStatus();
	String quote = order.getQuoteType();
	if (order != null && orderType == OrderType.LIMIT) {
	    if (quote.equals("bid")) {
		update(this.bids, order);
	    }  else if (quote.equals("ask")) {
		update(this.asks, order);
	    }
	}
    }
    private void update(PriorityQueue<Order> orderbook, Order order) {
	for (Order placedOrders : orderbook)
	    if (orderbook != null
		&& order.getOrderID().equals(placedOrders.getOrderID())) {
		order.setOrderPrice(new Scanner(System.in).nextDouble());
		order.setOrderVolume(new Scanner(System.in).nextInt());
		break;
	    }
    }
    /**
     * Methods for executing buy and sell 
     * limit and market orders.
     */
    private void executeBuyMarketOrders() {
	for (int i=0; i<bids.getSize();i++) 
	    if (bids.get(i)!= null
		&& bids.get(i).getOrderType()  == OrderType.MARKET) {
		int filledVolume = bids.get(i).getOrderFilledVolume();
		int remainingVoume = bids.get(i).getOrderVolume() - filledVolume;
		if (asks.getMin() == null) return;
		Iterator<Order> iterator = asks.iterator();
		if (!iterator.hasNext()) return;
		Order bestSellOrder = iterator.next();
		while (iterator.hasNext() && filledVolume
		       <= bids.get(i).getOrderVolume()) {
		    boolean cond = compareAndExecuteOrders(bids.get(i), bestSellOrder);
		    if (cond) break;
		    filledVolume = bids.get(i).getOrderFilledVolume();
		    try { bestSellOrder = iterator.next(); }
		    catch (NoSuchElementException e) {}
		}
	    }
    }
    private void executeSellMarketOrders() {
	for (int i=0; i<asks.getSize(); i++) 
	    if (asks.get(i)!=null
		&& asks.get(i).getOrderType() == OrderType.MARKET) {
		if (bids.getMax() == null) return;
		int filledVolume = asks.get(i).getOrderFilledVolume();
		int remainingVoume = asks.get(i).getOrderVolume() - filledVolume;
		for (int j = bids.getSize() - 1; j >= 0; j--) {
		    if (filledVolume == asks.get(i).getOrderVolume()) break;
		    boolean cond = compareAndExecuteOrders(asks.get(i), bids.get(j));
		    if (cond) break;
		    filledVolume = asks.get(i).getOrderFilledVolume();
		}
	    }
    }
    public void executeMarketOrders() {
	if (asks != null && bids != null) {
	    executeBuyMarketOrders();
	    executeSellMarketOrders();
	}
    }
    public void executeLimitOrders() {
	if (asks != null && bids != null) {
	    executeBuyLimitOrders();
	    executeSellLimitOrders();
	}
    }
    private void executeSellLimitOrders() {
	for (int i=0; i < asks.getSize(); i++) 
	    if (asks.get(i).getOrderType()
		== OrderType.LIMIT) 
		for (int j=0; j < bids.getSize(); j++)
		    if (bids.get(j).getOrderPrice()
			>= asks.get(i).getOrderPrice()) 
			compareAndExecuteOrders(asks.get(i), bids.get(j));
    }
    private void executeBuyLimitOrders() {
	for (int i=0; i < bids.getSize(); i++) 
	    if (bids.get(i).getOrderType()
		== OrderType.LIMIT) 
		for (int j=0; j < asks.getSize(); j++) 
		    if (asks.get(j).getOrderPrice()
			<= bids.get(i).getOrderPrice())
			compareAndExecuteOrders(bids.get(i), asks.get(j));
    }
    /**
     * Set of private methods for matching buy and sell orders,
     * and dealing with discrepancies in volume.
     */   
    private boolean compareAndExecuteOrders(Order order, Order orderToMatch) {
	double matchPrice = orderToMatch.getOrderPrice();
	double matchVolume = orderToMatch.getOrderVolume();
	if (order.getOrderVolume() < matchVolume) {
	    fillOrder(order, orderToMatch);
	    return true;
        } else if (order.getOrderVolume() == matchVolume) {
	    matchBuySellOrder(order, orderToMatch);
	    return true;
        } else if (order.getOrderVolume() > matchVolume) {
	    fillOrderPartially(order, orderToMatch);
	}
	return false;
    }
    private void fillOrderPartially(Order order, Order bestOffer) {
	double bestPrice = bestOffer.getOrderPrice();
	order.setOrderPrice(bestPrice);
	order.setOrderFilledVolume(bestOffer.getOrderVolume());
	bestOffer.setOrderStatus(OrderStatus.COMPLETED);
        removeOrder(bestOffer);
    }
    private void fillOrder(Order order, Order bestOffer) {
	int filledVolume = order.getOrderFilledVolume();
	bestOffer.setOrderFilledVolume(order.getOrderVolume() - filledVolume);
	order.setOrderStatus(OrderStatus.COMPLETED);
	removeOrder(order);
    }
    private void matchBuySellOrder(Order order, Order bestOffer) {
	order.setOrderStatus(OrderStatus.COMPLETED);
	bestOffer.setOrderStatus(OrderStatus.COMPLETED);
	removeOrder(order);
	removeOrder(bestOffer);
    }
    public boolean matches(PriorityQueue<Order> orders, Order order) {
	return orders.contains(order);	
    }
    /**
     * String representation of all orders, both buys and sells, 
     * in the Orderbook.
     * forEach is a possible alternative to the explicit use of for loops.
     */
    @Override
    public String toString() {
	StringBuilder strBids = new StringBuilder("");
	StringBuilder strAsks = new StringBuilder("");
	bids.getPriorityQueue()
	    .forEach(o -> strBids
		     .append(String.format("%n%s%n %s%n %s%n %s%n %s%n %s%n %s%n %s%n %s%n",
					   " | Buy Orders ",
					   "| Time Stamp: "+o.timeStampToString(),
					   "| Price: "+o.getOrderPrice(),
					   "| Volume: "+o.getOrderVolume(),
					   "| Filled volume: "+o.getOrderFilledVolume(),
					   "| Type of order:" +o.getOrderType(),
					   "| Quote: "+o.getQuoteType(),
					   "| Status: "+o.getOrderStatus(),
					   "| Order ID: "+o.getOrderID())));	
	asks.getPriorityQueue()
	    .forEach(o -> strAsks
		     .append(String.format("%n%s%n %s%n %s%n %s%n %s%n %s%n %s%n %s%n %s%n",
					   " | Sell Orders ",
					   "| Time Stamp: "+o.timeStampToString(),
					   "| Price: "+o.getOrderPrice(),
					   "| Volume: "+o.getOrderVolume(),
					   "| Filled volume: "+o.getOrderFilledVolume(),
					   "| Type of order: "+o.getOrderType(),
					   "| Quote: "+o.getQuoteType(),
					   "| Status: "+o.getOrderStatus(),
					   "| Order ID: " +o.getOrderID())));	
	return new StringBuilder(strBids).append(strAsks).toString();
    }
}
    
