import java.util.*;
import java.text.DecimalFormat;
import java.util.stream.*;
import java.io.Serializable;

/**
 * Abstract representation of the
 * parent class, i.e., Share
 */

public abstract class Share implements Comparable<Share>, Serializable  {
    
    private String shareType;
    private double currentPrice;
    private double lastPrice;
    private double nominalValue;
    private double earningsPerShare;    
   
    public Share(double currentPrice,
		 double lastPrice,
		 double nominalValue,
		 double earningsPerShare) {

	this.currentPrice = currentPrice;
	this.lastPrice = lastPrice;
	this.nominalValue = nominalValue;
	this.earningsPerShare = earningsPerShare;	
    }
    /**
     * Getters and setters for retrieving and 
     * mutating each instance variable value
     */
    public void setCurrentPrice(double currentPrice) {
	this.currentPrice = currentPrice;
    }
    public void setLastPrice(double lastPrice) {
	this.lastPrice = lastPrice;
    }
    public void setNominalValue(double nominalValue) {
	this.nominalValue = nominalValue;
    }
    public void setEarningsPerShare(double earningsPerShare) {
	this.earningsPerShare = earningsPerShare;
    }
    public abstract void setVolatility();
   
    public double getCurrentPrice() {
	return currentPrice;
    }
    public double getLastPrice() {
	return lastPrice;
    }
    public double getNominalValue() {
	return nominalValue;
    }
    public double getEarningsPerShare() {
	return earningsPerShare;
    }
    /*
      Abstract methods, whose implementation
      varies amongst the child classes, i.e., 
      Preferred Share, Ordinary Share, Unlisted Share
     */
    public abstract double getVolatility();
    public abstract String getDescriptor();    
    public abstract double getDividendPriceRatio();

    /*
      Accessory methods for calculating both
      the price/earnings ratio and the change
      in stock price
     */
    public double PriceEarningsRatio() {
	return currentPrice / earningsPerShare;
    }
    public double changeInStockPrice() {
	return ((currentPrice - lastPrice) / lastPrice) * 100;
    }
    /**
     * toString() that returns a string description of the corresponding
     class and its instance variables.
     */
    @Override
    public String toString() {
	return "\nLast price: " + lastPrice + "\nCurrent price: " + currentPrice
	    + "\nChange in stock price (in %): " +  (new StringBuilder()).
	    append((changeInStockPrice() < 0) ? "Down by: \u2198 " : "Up by: \u2197 ").toString()
	    + ((new DecimalFormat("###.###")).format(Math.abs(changeInStockPrice())));
    }
    /**
     * Provided implementation for compareTo as present in
     the Comparable<E> interface; particularly useful for
     comparing shares based on their current prices.
     */
    @Override
    public int compareTo(Share shareToCompare) {
	return Comparator.comparingDouble((Share share) -> share.currentPrice)
	    .thenComparingDouble(share -> share.lastPrice)
	    .compare(this, shareToCompare);
    }  
}

