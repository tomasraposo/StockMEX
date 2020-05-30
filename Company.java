import java.util.*;
import java.io.Serializable;

/**
 * Company class.
*/

public class Company implements Serializable {
    
    /**
     * Each instance variable represents a 
     * specific attribute that each 
     * company listed in a given trading 
     * venue should have.
     */
    private CompanyStockInfo stock;
    private String name;
    private double shareCapital;
    private HashMap<Share, Integer> issuedShares;
    private double marketCapital;

    public Company(String name) {
        this.name = name;
    }
    /**
     * Getters and setters for retrieving and 
     * mutating each instance variable value.
     */
    public void setCompanyStockInfo(ArrayList<Stock> stocks) {
	stock = new CompanyStockInfo(stocks);
	setOrderBook(new OrderBook());
	setIssuedShares();
    }
    public void setCompanyName(String name) {
	this.name = name;
    }
    public void setShareCapital(double shareCapital) {
	this.shareCapital = shareCapital;
    }
    /**
     * Hashmap to keep for each company key/value pairs
     * of type of share and its corresponding number of issued shares.
     */
    public void setIssuedShares() {
	HashMap<Share, Integer> dictVolumePerShareType = new HashMap<Share, Integer>();
	for (Stock stck : stock.getStockList()) {
	    dictVolumePerShareType.put(stck.getShareType(), (Integer) new Random().nextInt(1000) + 10);
	}
	issuedShares = dictVolumePerShareType;
    }
    public void setMarketCapital(double marketCapital) {
	this.marketCapital = marketCapital;
    }
    public void setMarketCapital() {
     for (Stock stck : stock.getStockList()) {
	    marketCapital = issuedShares.get(stck.getShareType())
		* stck.getShareType().getCurrentPrice();
	}
    }
    public void setOrderBook(OrderBook newOrderBook) {
	stock.setOrderBook(newOrderBook); 
    }
    public ArrayList<Stock> getCompanyStock() {
	return stock.getStockList();
    }
    public OrderBook getOrderBook() {
	if (stock.getOrderBook() != null) {
	    return stock.getOrderBook();
	} else {
	    stock.setOrderBook(new OrderBook());
	    return stock.getOrderBook();
	}
    }
    public String getCompanyName() {
	return name;
    }
    /**
     * Retrieve the corresponding number of issued shares
     * based on its type, i.e., Preferred, Ordinary, Unlisted
     */
    public int getIssuedSharesPerType(String descriptor) {
	for (Map.Entry elem : issuedShares.entrySet()) {
	    if (((Share)elem.getKey()).getDescriptor().equals(descriptor)) {
		return (int) elem.getValue();
	    }
	}
	return -1;
    }
    public String getIssuedShares() {
	StringBuilder issuedStr = new StringBuilder();
	for (Share share : issuedShares.keySet()) {
	    //issuedStr.append(share.getDescriptor() + ": " + issuedShares.get(share));
	    issuedStr.append(issuedShares.get(share));	    
	}
	return issuedStr.toString();
    }
    public double getMarketCapital() {
	return marketCapital;
    }
    public double getCurrentPrice() {
	return getCompanyStock().get(0).getShareType().getCurrentPrice();
    }
    public int getCompanyStockVolume() {
	return getCompanyStock().get(0).getVolume();
    }
    @Override
    public String toString() {
	return "Company: "+name+
	    "\nShare Capital: "+shareCapital+
	    "\nIssued Shares: "+getIssuedShares()+
	    "\nMarket Capital: "+marketCapital;
    }
    /**
     * Inner class for dynamically (at run-time) 
     * setting the actual stock information
     * specific to each company and its orderbook
     */
    private final class CompanyStockInfo implements Serializable {
        ArrayList<Stock> companyStock;
	OrderBook orderBook;
	CompanyStockInfo(ArrayList<Stock> stock) {
	    companyStock = stock;
	    orderBook = null;
	}
	ArrayList<Stock> getStockList() {
	    return companyStock;
	}
	OrderBook getOrderBook() {
	    return orderBook;
	}
	void setOrderBook(OrderBook orderBook) {
	    this.orderBook = orderBook;
	}
    }
}
