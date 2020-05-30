import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.table.AbstractTableModel;
/**
 * CompanyPanel
 *
 * This is the most important class in the GUI
 * as it leads with incoming buy/sell orders
 * and takes care of updating the actual orderbook
 */
@SuppressWarnings("serial")
public class CompanyPanel extends JPanel implements ChangeListener, Serializable {
    private JTabbedPane tabbedPane;
    private JPanel[] buysOrdersPanels;
    private JPanel[] sellsOrdersPanels;
    private JPanel[] companyPanels;
    private JTable[] buysTables;
    private JTable[] sellsTables;
    private JScrollPane[] buysScrolls;
    private JScrollPane[] sellsScrolls;        
    private ArrayList<Company> listedCompanies;
    private StockExchange stockEx;
    /**
     * The constructor handles the initialisation
     * and positioning of each component 
     * which are then layered accordingly.
     */
    public CompanyPanel(StockExchange stockEx) {
	super(new GridLayout(1, 1));
	setBorder(BorderFactory.createTitledBorder("Companies"));
	listedCompanies = stockEx.getListedCompanies();

	final int COMPANIES_NUM = listedCompanies.size();
    
        buysOrdersPanels = new JPanel[COMPANIES_NUM];
	sellsOrdersPanels = new JPanel[COMPANIES_NUM];
	companyPanels = new JPanel[COMPANIES_NUM];

        buysTables = new JTable[COMPANIES_NUM];
	sellsTables = new JTable[COMPANIES_NUM];        
	buysScrolls = new JScrollPane[buysTables.length];
        sellsScrolls = new JScrollPane[sellsTables.length];
        
	setBuys();
	setSells();
        
	setBuySellPanels();	
	setCompaniesPanel();
	
	tabbedPane = new JTabbedPane();
	setTabbedPane();
        
	tabbedPane.addChangeListener(this);
	setCompanyStockIndicators();
	
	add(tabbedPane);
        setOpaque(true);
    }
    public void loadSavedCompanies(StockExchange stockEx) {
	listedCompanies = stockEx.getListedCompanies();
    }
    public void setStockExchange(StockExchange stockEx) {
	//this.stockEx = stockEx;
    }
    /**
     * Upon an event linked to the JComboBox "companies",
     * the top header with the all of the company's stock 
     * information is displayed.
     * The user can choose which company to inspect.
     */
    public void setCompanyStockIndicators() {
	JPanel stockIndicators = new JPanel();
	setLayout(new FlowLayout());
	JLabel descriptor = new JLabel("Stock Info");
        String[] companiesNames = new String[listedCompanies.size()];
	for (int i=0; i<listedCompanies.size();i++) {
	    for (Stock stock : listedCompanies.get(i).getCompanyStock()) {
		companiesNames[i] = stock.getTicker();
	    }
	}
	JComboBox companies = new JComboBox(companiesNames);
        companies.setActionCommand("COMPANY");
        companies.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals("COMPANY")) {
			String item  = (String) companies.getSelectedItem();
			Component[] components = stockIndicators.getComponents();
			for (int i=0; i <components.length; i++) {
			    if (components[i].getClass().equals(JPanel.class)) {
				stockIndicators.remove((JPanel) components[i]);
			    }			   
			}
		        displayCompanyInfo(stockIndicators, item);
			stockIndicators.updateUI();
			stockIndicators.repaint();
        	    }
		}
	    });
	stockIndicators.add(descriptor, BorderLayout.PAGE_START);
	stockIndicators.add(companies);

	add(stockIndicators, BorderLayout.PAGE_START);

	revalidate();
	repaint();
    }
    /**
     * Adds a top header to the JTabbedPane displaying all sorts 
     * of stock specifc information. 
     */
    public void displayCompanyInfo(JPanel panel, String item) {
	String[] header = new String[]{
	    "Company",
	    "Issued",
	    "Type",
	    "MarketCap",
	    "P/E",
	    "Current P.",
	    "Last P.",
	    "(+/-) %"};		
	String[][] info = new String[1][7];
	Company company = Utils.lookupCompany(listedCompanies, item);
	for (Stock stock : company.getCompanyStock()) {
	    if (item.equals(stock.getTicker())) {
		info[0] = new String[]{
		    stock.getTicker(),
		    String.valueOf(company.getIssuedShares()),
		    stock.getShareType().getDescriptor(),
		    String.valueOf(company.getMarketCapital()),
		    String.valueOf(stock.getShareType().PriceEarningsRatio()),
		    String.valueOf(stock.getShareType().getCurrentPrice()),
		    String.valueOf(stock.getShareType().getLastPrice()),
		    stock.getShareType().changeInStockPrice() < 0 ?
		    "\u2198 " : "\u2197 "
			+ (new DecimalFormat("###.###"))
		    .format(Math.abs((stock.getShareType().changeInStockPrice())))};
	    }
	}
	DefaultTableModel table = new DefaultTableModel(info, header);
	JTable companyInfo = new JTable(table);
	companyInfo.setFillsViewportHeight(true);
	companyInfo.setBounds(30, 40, 100, 200);
	JPanel newPanel = new JPanel(new GridLayout(2,3));
	newPanel.add(companyInfo.getTableHeader());
        newPanel.add(companyInfo);
	panel.add(newPanel);
    }
    public ArrayList<Company> getListedCompanies() {
	return listedCompanies;
    }
    public JPanel[] getCompaniesTabbedPane() {
	return companyPanels;
    }
    public void setCompaniesTabbedPane(JPanel[] compPanels) {
        companyPanels = compPanels;
	String[] tickers = new String[companyPanels.length];
	for (int i=0; i<tabbedPane.getTabCount();i++) {
	    tickers[i] = tabbedPane.getTitleAt(i);
            tabbedPane.remove(i);
	}	
	for (int i=0; i<compPanels.length; i++) {
	    tabbedPane.add(tickers[i], compPanels[i]);
	}	
    }
    /**
     * Assign each buy/sell panel the corresponding JScrollPane
     * which contains the corresponding JTable
     */
    public void setBuySellPanels() {
	for (int i=0;i<buysOrdersPanels.length;i++) {
	    buysOrdersPanels[i] = new JPanel(new BorderLayout());
	    buysOrdersPanels[i].add(buysScrolls[i]);
	    buysOrdersPanels[i].setBorder(BorderFactory.createTitledBorder("Buy Orders"));
	    
	    sellsOrdersPanels[i] = new JPanel(new BorderLayout());
	    sellsOrdersPanels[i].add(sellsScrolls[i]);
	    sellsOrdersPanels[i].setBorder(BorderFactory.createTitledBorder("Sell Orders"));
	}
    }
    /**
     * Assign each companyPanel the corresponding buy/sell orderspanel.
     */
    public void setCompaniesPanel() {
	for (int i=0; i<companyPanels.length; i++) {
	    companyPanels[i] = new JPanel(new GridLayout(1, 1));
	    companyPanels[i].add(buysOrdersPanels[i], BorderLayout.NORTH);
	    companyPanels[i].add(sellsOrdersPanels[i], BorderLayout.SOUTH);
	}
    }
    /**
     * Sets the entire JTabbedPane with the tabs labeled
     * after each stock's ticker and each panel is a companyPanel.
     */
    public void setTabbedPane() {
	for (int i=0; i<listedCompanies.size(); i++) {
	    Company company = listedCompanies.get(i);
	    for (int j=0; j<company.getCompanyStock().size(); j++) {
		String ticker = company.getCompanyStock()
		    .get(j)
		    .getTicker();
		tabbedPane.addTab(ticker, companyPanels[i]);
		break;
	    }
	}
    }
    public void stateChanged(ChangeEvent e) {}
    /**
     * Add an order calling the addOrder() method in the OrderBookTable
     * class; The table is thus updated.
     */
    public void addOrder(Order order, String ticker) {
	int index = Utils.lookupCompanyIndex(listedCompanies, ticker);
	if (order.getQuoteType().equals("bid")) {
	    OrderBookTable table = (OrderBookTable) buysTables[index].getModel();
	    table.addOrder(order);
	} else {
	    OrderBookTable table = (OrderBookTable) sellsTables[index].getModel();
	    table.addOrder(order);
	}
    }
    /**
     * Update an order calling the updateRow() method in the OrderBookTable
     * class; The table is thus updated.
     */
    public void updateOrder(Order order, String ticker) {
	int index = Utils.lookupCompanyIndex(listedCompanies, ticker);
	if (order.getQuoteType().equals("bid")) {
	    OrderBookTable table = (OrderBookTable) buysTables[index].getModel();
	    table.updateRow(order);
	} else {
	    OrderBookTable table = (OrderBookTable) sellsTables[index].getModel();
	    table.updateRow(order);	
	}
    }
    /**
     * Cancel an order calling the removeRow() method in the OrderBookTable
     * class; The table is thus updated.
     */
    public void cancelOrder(Order order, String ticker) {
	int index = Utils.lookupCompanyIndex(listedCompanies, ticker);
	if (order.getQuoteType().equals("bid")) {
	    OrderBookTable table = (OrderBookTable) buysTables[index].getModel();
	    table.removeRow(order);
	} else {
	    OrderBookTable table = (OrderBookTable) sellsTables[index].getModel();
	    table.removeRow(order);	
	}
    }
    /**
     * Sets all active sell orders in the orderbook of each
     * company listed in the exchange.
     *
     * Each JTable (element) in buysTables is initialised, after being passed
     * an instance of the OrderBookTable class.
     */
    public void setBuys() {
	int i=0;
	int k=0; 
	for (;i<buysScrolls.length;i++) {
	    for (;k <listedCompanies.size();) {

		Company company = listedCompanies.get(k);
		OrderBook orderBook = company.getOrderBook();
		PriorityQueue<Order> buys = orderBook.getBids();
		OrderBookTable table = new OrderBookTable(buys);

		for (int l=0; l<buys.getSize();l++) {
		    Order order = buys.get(l);
		    table.addOrder(order);
		}
	        buysTables[i] = new JTable(table);
		buysTables[i].setBounds(30, 40, 100, 200);
		buysScrolls[i] = new JScrollPane(buysTables[i]);
		buysScrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        k++;
		break;	
	    }
	}
    }
    /**
     * Sets all active sell orders in the orderbook of each
     * company listed in the exchange.
     *
     * Each JTable (element) in sellsTables is initialised, after being passed
     * an instance of the OrderBookTable class.
     */
    public void setSells() {
	int i=0;
	int k=0;
	for (;i<sellsScrolls.length;i++) {
	    for (;k<listedCompanies.size();) {

		Company company = listedCompanies.get(k);
		OrderBook orderBook = company.getOrderBook();
		PriorityQueue<Order> sells = orderBook.getAsks();
		OrderBookTable table = new OrderBookTable(sells);

		for (int l=0; l<sells.getSize();l++) {
		    Order order = sells.get(l);
		    table.addOrder(order);
		}
		sellsTables[i] = new JTable(table);
		sellsTables[i].setBounds(30, 40, 100, 200);
		sellsScrolls[i] = new JScrollPane(sellsTables[i]);
		sellsScrolls[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        k++;
		break;	
	    }
	}
    }
    /**
     * Representation of the inner workings of a table
     * suitable for representing a dynamically updatable
     * orderbook.
     *
     *
     * The orderbook table takes a priority queue, whose 
     * ordering of the elements is dictated by the actuall
     * data structure.
     *
     *
     * Below are a set of methods that allow to interact with
     * the OrderBook Table:
     *
     * addOrder();
     * getRow();
     * updateRow();
     * removeRow();
     *
     * Each time an Order is placed an event is triggered, notifying
     * all registered listeners that a given row, cell or even
     * the whole table have changed.
     */
    private class OrderBookTable extends AbstractTableModel {
	private String[] columns = new String[] {"Order ID",
						 "Order type",
						 "Price",
						 "Volume",
						 "Status",
						 "Time stamp"};
	private PriorityQueue<Order> data;

	OrderBookTable(PriorityQueue<Order> orderBook) {
	    data = orderBook;
	}
	public int getColumnCount() {
	    return columns.length;
	}
	public int getRowCount() {
	    return data.getSize();
	}
	public String getColumnName(int index) {
	    return columns[index];
	}
	public Object getValueAt(int row, int col) {
	    Order order = data.get(row);
	    if (col == 0) 
		return order.getOrderID();
	    else if (col == 1)
		return order.getOrderType();
	    else if (col == 2)
		return order.getOrderPrice();
	    else if (col == 3)
		return order.getOrderVolume();
	    else if (col == 4)
		return order.getOrderStatus();
	    else
		return order.getTimeStamp();
	}
        public void setValueAt(Object value, int row, int col) {
	    Order order = data.get(row);
	    if (col == 2) 
	    	order.setOrderPrice((Double)value);
	    else if (col == 3)
		order.setOrderVolume((Integer) value);
	    
	    fireTableCellUpdated(row, col);
	}
	public Class getColumnClass(int c) {
	    return getValueAt(0, c).getClass();
	}
	public void addOrder(Order order) {
	    data.insert(order);
	    int index = getRow(order);
	    fireTableRowsInserted(index, index);
	}
	public int getRow(Order order) {
	    for (int i=0; i<getRowCount();i++) 
		if (order.getOrderID().equals(getValueAt(i,0)))
		    return i;
	    return -1;
	}
	public void updateRow(Order order) {
	    for (int i=0; i<getRowCount(); i++) {
        	if (order.getOrderID().equals(getValueAt(i, 0))) {
		    setValueAt(order.getOrderPrice(), i, 2);
		    setValueAt(order.getOrderVolume(), i, 3);
		    fireTableRowsUpdated(i, i);
		    break;
		}
	    }
	}
	public void removeRow(Order order) {
	    for (int i=0; i<getRowCount(); i++) {
        	if (order.getOrderID().equals(getValueAt(i, 0))) {
		    removeRow(order);
		    fireTableRowsDeleted(i, i);
		    break;
		}
	    }
	}
    }
}
