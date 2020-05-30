import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;
import java.io.Serializable;
/**
 * Order class
 */

public class Order implements Comparable<Order>, Quotable, Serializable {
    /**
     * Each instance variable represents a 
     * specific attribute that each 
     * order placed in a given trading 
     * venue should have.
     */

    private LocalTime timeStamp;
    private double price;
    private int volume;
    private int filledVolume;
    private OrderType orderType;
    private String quoteType;
    private OrderStatus orderStatus;
    private String orderID;
    
    public Order(double price, int volume, OrderType orderType,
		 String quoteType, OrderStatus orderStatus) {

	timeStamp = LocalTime.now();
	this.price = price;
	this.volume = volume;
	this.filledVolume = 0;
	this.orderType = orderType;
	this.quoteType = quoteType;
	this.orderStatus = orderStatus;
	orderID = generateOrderID();
    }
    /**
     * Getters and setters for retrieving and 
     * mutating each instance variable value.
     */

    public LocalTime getTimeStamp() {
	return timeStamp;
    }
    public double getOrderPrice() {
	return price;
    }	
    public int getOrderVolume() {
	return volume;
    }
    public int getOrderFilledVolume() {
	return filledVolume;
    }
    public OrderType getOrderType() {
      return orderType;
    }
    public String getQuoteType() {
	return quoteType;
    }
    public OrderStatus getOrderStatus() {
	return orderStatus;
    }
    public String getOrderID() {
	return orderID;
    }
    public void setOrderPrice(double newPrice) {
        price = newPrice;
    }	
    public void setOrderVolume(int newVolume) {
	volume = newVolume;
    }
    public void setOrderFilledVolume(int volumeFilled) {
	if (filledVolume < volume) 
	    filledVolume += volumeFilled;
	else
	    filledVolume = volumeFilled;
    }
    public void setQuoteType(String quoteType) {
	this.quoteType = quoteType;
    }
    public void setOrderStatus(OrderStatus orderStatus) {
	this.orderStatus = orderStatus;
    }
    /**
     * Equality by means of having the same order ID
     */
    @Override
    public boolean equals(Object object) {
	if (object instanceof Order) {
	    Order order = (Order) object;
	    if (this.orderID.equals(order.getOrderID())) {
		return true;
	    }
	}
	return false;
    }
    /**
     * Provided implementation for compareTo as present in
     the Comparable<E> interface; particularly useful for
     comparing orders based on their prices.
     */
    public int compareTo(Order compOrder) {
	return Comparator.comparingDouble((Order order) -> order.getOrderPrice())
	    .compare(this, compOrder);
    }
    /**
     * Returns a string representation of its time stamp,
     * formatted without an offset 
     */
    public String timeStampToString() {
	return timeStamp.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
    /**
     * Returns a string representation of a given order 
     * based on its instance variables values.
     */
    public String toString() {
	return String.format("%f %d %d %s %s %s",
			     price,
			     volume,
			     filledVolume,
			     orderType,
			     quoteType,
			     timeStampToString());
    }
    /**
     * Generates a unique order ID
     */
    public String generateOrderID() {
        return String.valueOf(this.hashCode()).chars()
	    .collect(StringBuilder::new,
		     StringBuilder::append,
		     StringBuilder::append).toString().substring(0, 10);
    }
}
