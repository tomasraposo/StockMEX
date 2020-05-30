import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.event.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.Serializable;
import javax.swing.table.AbstractTableModel;

/**
 * Panel for displaying and interacting
 * with the trader's 
 * account related aspects, e.g., 
 * balance, applicable fees, 
 */
@SuppressWarnings("unchecked")
public final class AccountPanel extends JPanel implements Serializable {

    private JTabbedPane accountPanel;
    private JPanel balancePanel;
    private JPanel inputPanel;
    private JTextField addBalance;
    private JButton addBalanceButton;
    private JPanel accountFeesPanel;
    private JTextField displayBalanceFeesField;
    private JComboBox balanceFees;
    private JTable portfolio;
    private JTable orderList;
    private Trader trader;
    private JScrollPane portfolioScrollPane;
    private JScrollPane orderListScrollPane;
    private String[] orderListIndicators;
    private StockExchange stockEx;
    /**
     * The constructor handles the initialisation
     * and positioning of each component 
     * which are then layered accordingly.
     */
    public AccountPanel(StockExchange stockEx) {
	super();
	setBorder(BorderFactory.createTitledBorder("Account"));
        setLayout(new BorderLayout(5, 5));
	this.stockEx = stockEx;
	trader = this.stockEx.getTrader();

	//Main Balance panel
	//
	balancePanel = new JPanel();
	balancePanel.setBorder(BorderFactory.createTitledBorder("Add balance"));
        balancePanel.setLayout(new BorderLayout(1, 1));

	
	//Add Balance (temp) Panel
	//
	inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addBalance = new JTextField(15);
	addBalanceButton = new JButton();
        addBalanceButton.setBounds(125, 90, 80, 30);
	addBalanceButton.setActionCommand("ADD");
	addBalanceButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals("ADD")) {
			addBalanceToAccount(addBalance);
		    }
		}
	    });
        
	inputPanel.add(addBalanceButton);
        inputPanel.add(addBalance);

	//Account Fees Panel
	//
	accountFeesPanel = new JPanel();
	accountFeesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        displayBalanceFeesField = new JTextField(10);
	balanceFees = new JComboBox(new String[]{
		"Balance",
		"Transaction fee",
		"Fund manager fee",
		"Customer fee"});
        balanceFees.setActionCommand("SELECTED");
	balanceFees.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals("SELECTED")) {
			String item  = (String) balanceFees.getSelectedItem();
		        viewBalanceAndFees(displayBalanceFeesField, item);
		    }
		}
	    });
        accountFeesPanel.add(balanceFees, BorderLayout.EAST);
	accountFeesPanel.add(displayBalanceFeesField, BorderLayout.SOUTH);
        accountFeesPanel.setBorder(BorderFactory.createTitledBorder("Balance & Fees"));
	
        balancePanel.add(inputPanel, BorderLayout.WEST);
        balancePanel.add(accountFeesPanel);
	
	//JTabbedPane : accountPanel
	accountPanel = new JTabbedPane();
		
        orderListIndicators = new String[] {"Order ID",
					    "Time Stamp"};
	setPortfolio();
	
	setOrders();

        accountPanel.addTab("Funds", balancePanel);
	accountPanel.addTab("Portfolio", portfolioScrollPane);
        accountPanel.addTab("Active Orders", orderListScrollPane);
	
	add(accountPanel, BorderLayout.CENTER);
	setOpaque(true);
    }
    /**
     * In setOrders() and setPortfolio() 
     * two JTables are created.
     * The DefaultTableModel is used so as to
     * allow for dynamic insertion of elements.
     *
     * Set active orders of a given trader
     */
    private void setOrders() {
	if (trader.getOrderList() != null) {
	    String[][] orders = new String[trader.getOrderList().size()][];
	    for (int i=0; i<trader.getOrderList().size(); i++) {
		Order order = trader.getOrderList().get(i);
		orders[i] = new String[]{
		    order.getOrderID(),
		    order.getTimeStamp().toString()};
	    }
	    DefaultTableModel defTable = new DefaultTableModel(orders, orderListIndicators);
	    orderList = new JTable(defTable);
	    //orderList.setBounds(10, 20, 100, 100);
	    orderListScrollPane = new JScrollPane(orderList);
	    orderListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
    }
    /**
     * Add placed order to the active orders list.
     */
    public void addOrder(Order order) {
        DefaultTableModel defTable = (DefaultTableModel) orderList.getModel();
	defTable.addRow(new String[]{
		order.getOrderID(),
		order.getTimeStamp().toString()});
    }
    /**
     * Set the trader's portfolio
     */
    private void setPortfolio() {
	if (trader.getPortfolio() != null) {
	    PortfolioTable table = new PortfolioTable(stockEx);
	    portfolio = new JTable(table);
	    portfolio.setBounds(10, 20, 100, 100);
	    portfolioScrollPane = new JScrollPane(portfolio);
	    portfolioScrollPane.setVerticalScrollBarPolicy(JScrollPane
							   .VERTICAL_SCROLLBAR_ALWAYS);
	}
    }
    /**
     * Add Order to traders portfolio if
     * getOrderStatus() == OrderStatus.COMPLETED 
     */
    public void addOrderToPortfolio(Stock stock) {
	PortfolioTable table = (PortfolioTable) portfolio.getModel();
	table.addStock(stock);
    }
    /**
     * Getters and setters for retrieving
     * and mutating the instance variables
     * values.
     */
    public Trader getTrader() {
	return trader;
    }
    /*
      sets the trader
     */
    public void setTrader(Trader trader) {
	this.trader = trader;
    }
    public JTabbedPane getTabbedPane() {
	return accountPanel;
    }
    public JScrollPane getOrderListPanel() {
        return orderListScrollPane;	
    }
    public JPanel getBalancePanel() {
	return balancePanel;
    }
    public JScrollPane getPortfolioPanel() {
	return portfolioScrollPane;
    }
    /*
     * Upon selection of item in JComboBox displays the corresponding
     * information in the JTextField component, "displayField".
     */
    private void viewBalanceAndFees(JTextField displayField, String item) {
	InvestmentAccount invAcc = (InvestmentAccount) trader.getAccount();
	if (item.equals("Balance")) {
	    displayField.setText(invAcc.getBalance()+"£");
        } else if (item.equals("Transaction fee")) {
	    displayField.setText(String.format("%3f", invAcc.getTransactionFee())+"£");
	} else if (item.equals("Fund manager fee")) {
	    displayField.setText(String.format("%3f", invAcc.getFundManagerFee())+"£");
	} else {
	    displayField.setText(String.format("%3f", invAcc.getCustomerFee())+"£");
	}
    }
    /**
     * Add balance to trader's account
     */
    private void addBalanceToAccount(JTextField balance) {
	try {
	    stockEx.getTrader().getAccount().addMoneyToBalance(Double.parseDouble(balance.getText()));
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(null, "Only numerical digits are accepted.");
	}
    }
    /**
     * Representation of the inner workings of a table
     * suitable for representing a dynamically updatable
     * portfolio.
     *
     *
     * The portfolio table takes an ArrayList<Stock>, whose
     * ordering is merely temporal. Hence, consecutive.
     *
     * Below are a set of methods that allow to interact with
     * the OrderBook Table:
     *
     * addStock();
     * getRow();
     *
     * Each time stock is added an event is triggered, notifying
     * all registered listeners that a given row, cell or even
     * the whole table have changed.
     */
    private class PortfolioTable extends AbstractTableModel {
	private String[] columns = new String[] {"Ticker",
						 "Price",
						 "Volume",};      
	private ArrayList<Stock> data;

	PortfolioTable(StockExchange stockEx) {
	   data = stockEx.getTrader().getPortfolio().getStocks();
	}
	public int getColumnCount() {
	    return columns.length;
	}
	public int getRowCount() {
	    return data.size();
	}
	public String getColumnName(int index) {
	    return columns[index];
	}
	public Object getValueAt(int row, int col) {
	    Stock stock = data.get(row);
	    if (col == 0) 
		return stock.getTicker();
	    else if (col == 1)
		return stock.getShareType().getCurrentPrice();
	    else 
		return stock.getVolume();
	}
        public void setValueAt(Object value, int row, int col) {
	    Stock stock = data.get(row);
	    if (col == 1) 
	        stock.getShareType().setCurrentPrice((Double)value);
	    else if (col == 2)
		stock.setVolume((Integer) value);
	    
	    fireTableCellUpdated(row, col);
	}
	public Class getColumnClass(int c) {
	    return getValueAt(0, c).getClass();
	}
	public void addStock(Stock stock) {
	    if (getRow(stock) == -1) {
		data.add(stock);
		int index = getRow(stock);
		fireTableRowsInserted(index, index);
	    } else {
		int index = getRow(stock);
		setValueAt(stock.getVolume(), index, index);
		fireTableCellUpdated(index, index);
	    }
	}
	public int getRow(Stock stock) {
	    for (int i=0; i<getRowCount();i++) 
		if (stock.getTicker().equals(getValueAt(i,0)))
		    return i;
	    return -1;
	}
    }
}
