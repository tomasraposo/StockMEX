import java.util.function.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.stream.*;
import java.util.stream.Stream;
import java.util.*;
import java.io.*;
import java.io.Serializable;
/**
 * A set of static methods useful for computing 
 * values and executing operations should not
 * be part of the code of a given class, i.e.,
 * reading company names from a file, computing
 * price estimations, generate random prices, etc.
 */
public final class Utils implements Serializable {

    public static double randomPriceGenerator() {
	return Double.parseDouble(new DecimalFormat("###.##")
				  .format(DoubleStream.builder()
					  .add(DoubleStream.of(new Random().nextDouble())
					       .map(n -> n * 10).sum())
					  .add(DoubleStream.of(new Random().nextDouble())
					       .map(n -> n * 5).sum()).build().sum()));
    }
    public static double estimateStockRates(Share share) {
        double random = new Random().nextDouble();
	double percentualChange = 2 * share.getVolatility() * random;
	
	if (percentualChange > share.getVolatility()) percentualChange -= 2 * share.getVolatility();
	double priceChange = share.getCurrentPrice() * percentualChange;
	
	return Double.parseDouble((new DecimalFormat("###.###")).format(share.getCurrentPrice() + priceChange));
    }
    public static String generateTicker(Company company) {
	StringBuilder ticker = new StringBuilder();
	for (char character : (company.getCompanyName()).toCharArray()) {
	    ticker.append((Character.isUpperCase(character) ? character : ""));
	}
	if (ticker.length() == 1 || ticker.length() == 2) {
	    ticker.append(Character.toString(((char)(new Random()).nextInt(10) + 107))
			  .toUpperCase())
		.append(Character.toString(((char)(new Random()).nextInt(20) + 97))
			  .toUpperCase());
	}
	return ticker.toString();
    }
    
    public static void readStockMarketListedTraders(ArrayList<Trader> listedTraders) {
	try {
	    Scanner scanner = new Scanner(new File("stockMarketListedTraders.txt"));
	    while (scanner.hasNextLine()) {
		Trader trader = new Trader(scanner.nextLine());
		listedTraders.add(trader);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    public static void readStockMarketListedCompanies(ArrayList<Company> listedCompanies) {
	try {
	    Scanner scanner = new Scanner(new File("stockMarketListedCompanies.txt"));
	    while (scanner.hasNextLine()) {
		Company company = new Company(scanner.nextLine());
		ArrayList<Stock> stocks  = generateArbitraryStockPerCompany(company);
		company.setCompanyStockInfo(stocks);
		stocks.get(0).setVolume(company);
		company.setMarketCapital();
		listedCompanies.add(company);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    public static ArrayList<Stock> generateArbitraryStockPerCompany(Company company) {
	return new ArrayList<Stock>() {{
	    add(new Stock(company, generateShare()));
	}};
    }
    public static Share generateShare() {
	return (new Random().nextDouble() > 0.5) ?
	    new PreferredShare(randomPriceGenerator(),
			       randomPriceGenerator(),
			       new Random().nextDouble(),
			       new Random().nextDouble(),
			       new Random().nextDouble()) :
	    new OrdinaryShare(randomPriceGenerator(),
			      randomPriceGenerator(),
			      new Random().nextDouble(),
			      new Random().nextDouble(),
			      new Random().nextDouble());
    }
    public static String priceChangeEstimationToString(Stock stock) {

	double estPrice = estimateStockRates(stock.getShareType());
	double currPrice = stock.getShareType().getCurrentPrice();
	
	Function <Double, Function<Double, Double>> priceChangeFunc = estimatedPrice
	    -> currentPrice
	    -> ((estimatedPrice - currentPrice) / currentPrice) * 100;
	
	StringBuilder str = new StringBuilder((stock.getTicker()))
	    .append(": ")
	    .append(estPrice);
	
	if (estPrice < currPrice) {
	    str.append("\nDown by: \u2198 ")
		.append(new DecimalFormat("###.###")
			.format(Math.abs(priceChangeFunc
					 .apply(estPrice)
					 .apply(currPrice)))).append("%");
	} else {
	    str.append("\nUp by: \u2197 ")
		.append(new DecimalFormat("###.###")
			.format(Math.abs(priceChangeFunc
					 .apply(estPrice)
					 .apply(currPrice)))).append("%");
	}
	return str.toString();
    }
    public static void getPriceEstimationOverTime(ArrayList<Company> listedCompanies, int time) {
	for (int i = 0; i < time; i++) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_WEEK, i);
	    
	    System.out.println(new StringBuilder("\n"+calendar.getTime().toString())
			       .append(":"));
	    
	    for (Company company : listedCompanies) {
		for (Stock stock : company.getCompanyStock()) {
		    System.out.println(priceChangeEstimationToString(stock));
		}
	    }
	}
    }
    public static double lookupStockPrice(ArrayList<Company> listedCompanies, String ticker) {
	for (Company company : listedCompanies) {
	    for (Stock stocks : company.getCompanyStock()) {
		if (stocks.getTicker().equals(ticker)) {
		    return stocks.getShareType().getCurrentPrice();
		}
	    }
	}
	return-1;
    }
    public static Company lookupCompany(ArrayList<Company> listedCompanies, String ticker) {
        for (Company company : listedCompanies)  {
	    for (Stock stocks : company.getCompanyStock()) {
		if (stocks.getTicker().equals(ticker)) {
		    return company;
		}
	    }
	}
	return null;
    }
    public static int lookupCompanyIndex(ArrayList<Company> listedCompanies, String ticker) {
	for (int i=0; i < listedCompanies.size(); i++)  {
	    for (Stock stocks : listedCompanies.get(i).getCompanyStock()) {
		if (stocks.getTicker().equals(ticker)) {
		    return i;
		}
	    }
	}
	return -1;
    }
    public static Order getPlacedOrderByOrderID(ArrayList<Company> listedCompanies, Order placedOrder) {
	if (placedOrder.getQuoteType().equals("bid")) {
	    for (Company company : listedCompanies) {
		for (Order order : company.getOrderBook().getBids()) {
		    if (order.getOrderID().equals(placedOrder.getOrderID())) {
			return order; 
		    }
		}
	    }
	} else {
	    for (Company company : listedCompanies) {
		for (Order order : company.getOrderBook().getAsks()) {
		    if (order.getOrderID().equals(placedOrder.getOrderID())) {
			return order; 
		    }
		}
	    }
	}
	return null;
    }
    public static boolean checkTicker(ArrayList<Company> listedCompanies, String ticker) {
	for (Company company : listedCompanies)  {
	    for (Stock stock : company.getCompanyStock()) {
		if (ticker.equals(stock.getTicker()))
		    return true;
	    }
	}
	return false;
    }
    public static void loading() {
	for (int i = 0; i <= 100; i++) {
            System.out.print("Executing all market orders..." + i + "% "+"\r");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }	
}
