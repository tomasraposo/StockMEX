import java.util.*;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
/**
 * Panel for interacting with the actual
 * Stock Exchange, i.e., StockMEX.
 * The registered trader can place new orders
 * or update and cancel a given placed order.
 */
public final class TradingPanel extends JPanel implements Serializable {
    private JTextField inputTicker;
    private JTextField inputPrice;
    private JTextField inputQuantity;
    private ButtonGroup bsGroup;
    private JRadioButton buyButton;
    private JRadioButton sellButton;
    private ButtonGroup mlGroup;
    private JRadioButton marketButton;
    private JRadioButton limitButton;
    private JButton updateButton;
    private JButton cancelButton;
    private JTextField orderID;
    private JTextField newPrice;
    private JTextField newQuantity;
    private StockExchange stockEx;
    private CompanyPanel companies;
    private AccountPanel account;
    private Trader trader;

    /**
     * The constructor handles the initialisation
     * and positioning of each component 
     * which are then layered accordingly.
     */
    public TradingPanel(StockExchange stockEx,
			CompanyPanel companies) {
	super();
	setBorder(BorderFactory.createTitledBorder("Trade"));
	this.stockEx = stockEx;
	this.companies = companies;
	
	inputTicker = new JTextField(15);
	JLabel tickerLabel = new JLabel("Enter ticker   ");
	inputTicker.setBounds(10, 50, 150, 100);
	
	inputPrice = new JTextField(15);
	JLabel priceLabel = new JLabel(" Enter price      ");
	inputPrice.setBounds(10, 50, 150, 100);
	
	inputQuantity = new JTextField(15);
	JLabel quantityLabel = new JLabel("Enter quantity   ");
	inputQuantity.setBounds(10, 50, 150, 100);  

	buyButton = new JRadioButton("Buy");
        buyButton.setBounds(120, 30, 120, 50);
	buyButton.setActionCommand("BUY");
	sellButton = new JRadioButton("Sell");
        sellButton.setActionCommand("SELL");
	sellButton.setBounds(250, 30, 80, 50);
	
	
	marketButton = new JRadioButton("Market");
        marketButton.setActionCommand("MARKET");
	marketButton.setBounds(125, 90, 80, 30);
	
	limitButton = new JRadioButton("Limit");
        limitButton.setActionCommand("LIMIT");
	limitButton.setBounds(20, 30, 150, 50);

	JButton orderButton = new JButton("Place order.");
	//orderButton.setActionCommand("PLACE_ORDER");
        ActionListener orderListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    placeOrder(bsGroup);
		}
	    };
        orderButton.addActionListener(orderListener);
	orderButton.setBounds(125, 90, 80, 30);
	
        bsGroup = new ButtonGroup();
	bsGroup.add(buyButton); bsGroup.add(sellButton);

	mlGroup = new ButtonGroup();
	mlGroup.add(marketButton); mlGroup.add(limitButton);

	
	JPanel buttonsPanel = new JPanel(new GridLayout(5, 2));
	buttonsPanel.setBorder(BorderFactory.createTitledBorder("Order type"));
        buttonsPanel.add(buyButton);
	buttonsPanel.add(sellButton);
	buttonsPanel.add(marketButton);
	buttonsPanel.add(limitButton);
        
	JPanel orderPanel = new JPanel(new GridLayout(10, 5));
	orderPanel.setBorder(BorderFactory.createTitledBorder("Place order"));
	orderPanel.add(tickerLabel);
	orderPanel.add(inputTicker);
	orderPanel.add(priceLabel);
	orderPanel.add(inputPrice);
	orderPanel.add(quantityLabel);
	orderPanel.add(inputQuantity);	
	orderPanel.add(orderButton);
	
	JPanel updateCancelPanel = new JPanel(new GridLayout(0, 1, 2, 3));
        updateCancelPanel.setBorder(BorderFactory.createTitledBorder("Update/Cancel order"));
	
	orderID = new JTextField(15);
	JLabel orderIDLabel = new JLabel("Order ID...      ");
        orderIDLabel.setBounds(20,75,250,200);
	
	newQuantity = new JTextField(15);
	JLabel newQuantityLabel = new JLabel("New quantity...      ");
        newQuantityLabel.setBounds(20,75,250,200);
	
	newPrice = new JTextField(15);
	JLabel newPriceLabel = new JLabel("New price...      ");
        newPriceLabel.setBounds(20,75,250,200);
	
	
        updateButton = new JButton("Update order.");
	ActionListener updateListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    updateOrder();
		}
	    };        
        updateButton.addActionListener(updateListener);
        updateButton.setActionCommand("UPDATE_ORDER");
        updateButton.setBounds(125, 90, 80, 30);
	
        cancelButton = new JButton("Cancel order.");
	ActionListener cancelListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    cancelOrder();
		}
	    };
        cancelButton.addActionListener(cancelListener);
        cancelButton.setActionCommand("CANCEL_ORDER");
        cancelButton.setBounds(125, 90, 80, 30);

	updateCancelPanel.add(orderIDLabel);
        updateCancelPanel.add(orderID);
	updateCancelPanel.add(newQuantityLabel);
        updateCancelPanel.add(newQuantity);
	updateCancelPanel.add(newPriceLabel);
        updateCancelPanel.add(newPrice);
	updateCancelPanel.add(updateButton);
	updateCancelPanel.add(cancelButton);

	JPanel lastPanel = new JPanel(new GridLayout(1, 1));
	lastPanel.add(buttonsPanel);
        lastPanel.add(orderPanel);
	lastPanel.add(updateCancelPanel);

	add(lastPanel);
    }
    /*
     * Connect the accountpanel with the 
     * tradingpanel via the trader.
     */
    public void connectAccountPanel(AccountPanel acc) {
	account = acc;
    }
    public void setUser(Trader trader) {
	this.trader = trader;
    }
    /**
     * Generate new orders with threads.
     */
    public void genNewOrders(String ticker) {
	new TaskTrigger().triggerAction(stockEx, ticker);
    }
    /**
     * Place a new order.
     *
     * The placed order will be registered in
     * the corresponding company's orderbook
     * and will be matched against all currently
     * active buy/sell orders via the trading execution mechanism.
     */
    private void processOrder(Order order, String ticker) {
	trader.addOrder(order);
	companies.addOrder(order, ticker);
	account.addOrder(order);
	Company company = Utils.lookupCompany(stockEx.getListedCompanies(), ticker);
	if (company != null) {
	    company.getOrderBook().addOrder(order);
	    stockEx.getTradingEngine().execute(company);
	}
	if (order.getOrderStatus() == OrderStatus.COMPLETED) {
	    Stock stock = new Stock(company.getCompanyStock().get(0));
	    stock.setVolume(order.getOrderVolume());
	    trader.getPortfolio().addStock(stock, order.getOrderVolume());
	    account.addOrderToPortfolio(stock);
	}
    }
    public void placeOrder(ButtonGroup bsGroup) {
	if (bsGroup.getSelection() != null
	    && mlGroup.getSelection() != null) {
	    ArrayList<Company> listedCompanies = stockEx.getListedCompanies();
	    String mlSelection = mlGroup.getSelection().getActionCommand();
	    String ticker = inputTicker.getText();
	    boolean isValid = Utils.checkTicker(listedCompanies, ticker);
	    if (!isValid) {JOptionPane.showMessageDialog(null,"Please type in capital letters"+
							 "the stock ticker correctly.");
		return;
	    }
	    int quantity = 0;
	    double price = 0;
	    try {quantity = Integer.parseInt(inputQuantity.getText());}
	    catch (NumberFormatException e) {return;}
	    switch (bsGroup.getSelection().getActionCommand()) {
	    case "BUY":
		if (trader.getAccount().getBalance() > 0) {
		    if (mlSelection.equals("MARKET")) {
		        price = Utils.lookupStockPrice(listedCompanies, ticker);
			Order order = trader.placeBuyOrder(price, quantity, OrderType.MARKET);
			processOrder(order, ticker);
		    } else {
			 try {price = Double.parseDouble(inputPrice.getText());}
			 catch (NumberFormatException e) {return;}
			 Order order = trader.placeBuyOrder(price,quantity, OrderType.LIMIT);
			 processOrder(order, ticker);
			 return;
		    }
		} else {
		    JOptionPane.showMessageDialog(null, "You have insufficient funds in your account.");
		}
		break;
	    case "SELL":
		if (trader.getPortfolio().getStocks().size() > 0) {
		    if (mlSelection.equals("MARKET")) {
			price = Utils.lookupStockPrice(listedCompanies, ticker);
			Order order = trader.placeSellOrder(price, quantity, OrderType.MARKET);
			processOrder(order, ticker);
		    } else {
			try {price = Double.parseDouble(inputPrice.getText());}
			catch (NumberFormatException e) {return;}
			Order order = trader.placeSellOrder(price, quantity,OrderType.LIMIT);
			processOrder(order, ticker);
		    }
		} else {
		    JOptionPane.showMessageDialog(null, "Your portfolio is currently empty.");
		}
		break;
	    }
	}
    }
    /**
     * Update a given order that has already been placed 
     * in the exchange.
     */
    private void processOrderVolumeUpdate(Order order, String ticker) {
	int quantity = Integer.parseInt(newQuantity.getText());
	order.setOrderVolume(quantity);       
	Order placedOrder = Utils.getPlacedOrderByOrderID(stockEx.getListedCompanies(),
							  order);
	if (placedOrder != null) placedOrder.setOrderVolume(quantity);
	companies.updateOrder(order, ticker);
    }
    private void processOrderPriceUpdate(Order order, String ticker) {
	double price = Double.parseDouble(newPrice.getText());
	if (price > 0) order.setOrderPrice(price);
	Order placedOrder = Utils.getPlacedOrderByOrderID(stockEx.getListedCompanies(),
							  order);
	if (placedOrder != null) placedOrder.setOrderPrice(price);
	companies.updateOrder(order, ticker);
    }
    private void processOrderUpdate(Order order, String ticker) {
        processOrderVolumeUpdate(order, ticker);
	processOrderPriceUpdate(order, ticker);
    }
    public void updateOrder() {
        String id = orderID.getText();
	String ticker = inputTicker.getText();
	try {
	    Order order = trader.getOrderList().stream()
		.filter(o -> o.getOrderID().equals(id)).findFirst().get();
	    if (newPrice.getText().length() == 0) {
	        processOrderVolumeUpdate(order, ticker);
	    } else if (newQuantity.getText().length() == 0) {
	        processOrderPriceUpdate(order, ticker);
	    }  else {
	        processOrderUpdate(order, ticker);		
	    }
	    JOptionPane.showMessageDialog(null, "Your order has been updated."); 
	} catch (NoSuchElementException e) {
	    JOptionPane.showMessageDialog(null, "The number you have typed in does not"+
					  "match any of your placed orders.");
	}
    }
    /**
     * Cancel a given order that has already been placed
     * in the exchange.
     */
    public void cancelOrder() {
	String id = orderID.getText();
	String ticker = inputTicker.getText();
	if (trader.getOrderList() != null) 
	    try {
		for (int i=0; i < trader.getOrderList().size(); i++) {
		    Order order = trader.getOrderList().get(i);
		    if (order.getOrderID().equals(id)) 
			for (Company company : stockEx.getListedCompanies()) 
			    if (company.getOrderBook() != null) 
				if (order.getQuoteType().equals("bid")) {
				    PriorityQueue<Order> bids = company.getOrderBook().getBids();
				    matchAndRemoveOrder(company, bids, order, ticker);
				    break;
				} else {
				    PriorityQueue<Order>asks = company.getOrderBook().getAsks();
				    matchAndRemoveOrder(company, asks, order, ticker);
				    break;
				}
		}
		JOptionPane.showMessageDialog(null, "Your order has been cancelled.");  
	    } catch (ConcurrentModificationException e) {
		JOptionPane.showMessageDialog(null, "An error has ocurred with the order cancelation system.");
	    }
    }
    private void matchAndRemoveOrder(Company company, PriorityQueue<Order> orderbook, Order order,
				     String ticker) {
	if (company.getOrderBook().matches(orderbook, order)) {
	    company.getOrderBook().removeOrder(order);
	    trader.getOrderList().remove(order);
	    companies.cancelOrder(order, ticker);
	}
    }
}
