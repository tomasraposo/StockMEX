import java.util.*;
import java.util.stream.*;
import java.io.Serializable;

/**
 * Portfolio class.
 * Each portfolio class
 * has an ArrayList<Stock>
 * that corresponds to the 
 * bought stock of a given Trader.
 */
public class Portfolio implements Serializable {

    private ArrayList <Stock> portfolio;
    /**
     * Two constructors:
     * The Former initializes a new portfolio.
     * The Latter creates a portfolio instance 
     * after an already initiliased ArrayList<Stock>
     */
    public Portfolio() {
	portfolio = new ArrayList<Stock>();
    }
    public Portfolio(ArrayList<Stock> stocks) {
	portfolio = stocks;
    }
    public ArrayList<Stock> getStocks() {
	return portfolio;
    }
    /**
     * Add stock to portfolio.
     * If it is listed in the portfolio
     * then increase its quantity.
     */
    public void addStock(Stock stock, int quantity) {        	
	if (isListed(stock.getTicker())) {
	    int i = indexOfStock(stock.getTicker());
	    portfolio.get(i).setVolume(portfolio.get(i).getVolume() + quantity);
	} else if (portfolio.indexOf(stock) == -1) {
	    portfolio.add(new Stock(stock));
	    portfolio.get(indexOfStock(stock.getTicker())).setVolume(quantity);
	}
    }
    /*
      Remove a given stock from the portfolio.
     */
    public void removeStock(Stock stock) {
	portfolio.remove(stock);
    }
    /**
     * Remove all stock from portfolio.
     */
    public void liquidatePortfolio() {
	portfolio.clear();
    }
    /**
     * Print out portfolio.
     */
    public void printPortfolio() {
	for (int i = 0; i < portfolio.size(); i++) {
	    System.out.println("\nTicker: "+portfolio.get(i).getTicker()+"\n"+
			       "Bid volume: " + portfolio.get(i).getVolume()+"\n"+
			       "Price bought at: £" + portfolio.get(i).getShareType()
			       .getCurrentPrice());
	}
    }
    /**
     * Returns true if a given company stock
     * based on its stock symbol (ticker) 
     * is present in the trader's portfolio.
     */
    public boolean isListed(String ticker) {
	if (portfolio == null) {
	    throw new NoSuchElementException();
	} else {
	    return portfolio.stream()
		.filter(t -> t.getTicker().equals(ticker))
		.findFirst().isPresent();
	}
    }
    /**
     * Returns the index of a given company stock
     * based on its stock symbol (ticker) 
     */
    public int indexOfStock(String ticker) {
	if (portfolio == null) {
	    throw new NoSuchElementException();
	} else {
	    return IntStream.range(0, portfolio.size())
		.filter(i -> portfolio.get(i).getTicker().equals(ticker))
		.findFirst()
		.orElse(-1);
	}
    }
    /**
     * returns the total valuation of the trader's portfolio
     */
    public double getPortfolioValuation() {
	double valuation = 0.0;
	for (Stock stocks : portfolio) {
	    double marketPrice = stocks.getShareType().getCurrentPrice();
	    double value = marketPrice * stocks.getVolume();
	    valuation += value;
	}
	return valuation;
    }
}
