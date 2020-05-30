import java.util.*;
import java.io.Serializable;
/**
 * Trader class.
 * Template for creating traders 
 * in the stock exchange.
 */
public class Trader implements Serializable {
    
    private String name;
    private Portfolio portfolio;
    private Account account;
    private String clientCode;
    private List<Order> orderList;
    /**
     * Two constructors:
     * the former creates a new trader 
     * after a given name.
     * the latter creates a new trader
     * after a given name passed as argument
     * and with a predefined balance and interest rate.
     */
    public Trader(String name) {
	this.name = name;
	portfolio = new Portfolio();
	account = new InvestmentAccount();
	clientCode = generateUniqueClientCode();
	orderList = new ArrayList<Order>();
    }
    public Trader(String name, double balance, double interest) {
	this.name = name;
	portfolio = new Portfolio();
	account = new InvestmentAccount(balance, interest);
	clientCode = generateUniqueClientCode();
	orderList = new ArrayList<Order>();
    }
    /**
     * Getters and setters for retrieving and mutating
     * each instance variable value.
     */
    public void setName(String name) {
	this.name = name;
    }
    public void setPortfolio(Portfolio portfolio) {
	this.portfolio = portfolio;
    }
    public void setAccount(Account account) {
	this.account = account;
    }
    public String getName() {
	return name;
    }
    public Portfolio getPortfolio() {
	return portfolio;
    }
    public Account getAccount() {
	return account;
    }
    public String getClientCode() {
	return clientCode;
    }
    public List<Order> getOrderList() {
	return orderList;
    }
    /**
     * add order to active orders list.
     */
    public void addOrder(Order order) {
	if (orderList != null)
	    orderList.add(order);
    }
    /**
     * Both placeBuyOrder and placeSellOrder
     * return a new Order.
     */
    public Order placeBuyOrder(double price, int quantity, OrderType orderType) {
	return new Order(price, quantity, orderType, "bid", OrderStatus.QUEUING);
    }
    public Order placeSellOrder(double price, int quantity, OrderType orderType) {
	return new Order(price, quantity, orderType, "ask", OrderStatus.QUEUING);
    }
    /**
     * generates a unique client code.
     */
    public String generateUniqueClientCode() {
	StringBuilder uniqueCode = new StringBuilder();;
	for (int i = 0; i < 9; i++) {
	    uniqueCode.append((i == 4) ? "-" : String.valueOf((new Random()).nextInt(10)));
	}
	return uniqueCode.toString();
    }
    @Override
    public String toString() {
	return (new StringBuilder(getName()))
	    .append(":\n" + getAccount().toString())
	    .append("Client code: "+clientCode).toString();
    }
}
