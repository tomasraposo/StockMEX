import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.border.LineBorder;
import java.io.*;
import java.io.Serializable;
import java.awt.event.WindowEvent;

/**
 * MainFrame is the actual Frame of the GUI
 * where all subclasses of JPanel are placed.
 * This class handles the file save/load via
 * JMenuBar "topBar".
 *
 * The user needs to select if he/she wants to
 * start a new day or load a past session.
 */

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements Serializable  {
    private JMenuBar topBar;
    private CompanyPanel companyPanel;
    private TradingPanel tradingPanel;
    private AccountPanel accountPanel;
    private PredictionPanel predictionPanel;
    private StockExchange stockEx;
    private Trader trader;
    
    public MainFrame(StockExchange stockEx, Trader trader) {
	super("StockMEX");
	setBounds(500,500,370,600);
	setSize(new Dimension(700, 800));

	this.stockEx = stockEx;
	this.trader = stockEx.getTrader();
	//Top bar
	topBar = new JMenuBar();
	JMenu fileSection = new JMenu("FILE");
	JMenu quit = new JMenu("QUIT");
	JMenu newDaySection = new JMenu("NEW DAY");
	
	JRadioButtonMenuItem quitButton = new JRadioButtonMenuItem();
        quitButton.setSelected(true);

	JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setActionCommand("QUIT");
	quitItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals("QUIT"))
			System.exit(0);
		}
	    });
        quitItem.setMnemonic(KeyEvent.VK_E);
        quitItem.setToolTipText("Quit");
        quit.add(quitItem);

	JRadioButtonMenuItem newDayButton = new JRadioButtonMenuItem();
        newDayButton.setSelected(true);

	/**
	 * If the option "New Day" is selected a new day at the stock exchange
	 * is started.
	 */
	JMenuItem newDay = new JMenuItem("New Day");
        newDay.setActionCommand("NEW_DAY");
        newDay.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals("NEW_DAY")) {
			/**
			 * This statement closes the previous Frame.
			 */
		        MainFrame.this.dispatchEvent(new WindowEvent(MainFrame.this,
								     WindowEvent.WINDOW_CLOSING));
			MainFrame.this.stockEx = new StockExchange();
			Utils.readStockMarketListedTraders(MainFrame.this.stockEx.getListedTraders());
			Utils.readStockMarketListedCompanies(MainFrame.this.stockEx.getListedCompanies());
			MainFrame.this.stockEx.setActiveOrders();
			MainFrame.this.stockEx.getTradingEngine().orderMatching();
			MainFrame mf = new MainFrame(MainFrame.this.stockEx, MainFrame.this.trader);
		    }
		}
	    });
        newDay.setMnemonic(KeyEvent.VK_E);
        newDay.setToolTipText("NEW DAY");
        newDaySection.add(newDay);
	
        ButtonGroup buttonsGroup = new ButtonGroup();
	JRadioButtonMenuItem loadButton = new JRadioButtonMenuItem();
        loadButton.setSelected(true);
	buttonsGroup.add(loadButton);

	
	/**
	 * If the option "Load file" is selected the user can choose which
	 * file to load as a starting point for the the stock exchange
	 */
	JMenuItem loadItem = new JMenuItem("Load file");
	loadItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("LOAD_FILE")) {
		    JFileChooser fileChooser = new JFileChooser("./");
		    int option = fileChooser.showOpenDialog(MainFrame.this);
		    if(option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
			    System.out.println(file.getAbsolutePath().toString());
			    FileInputStream fileIn = new FileInputStream(file);
			    ObjectInputStream objIn = new ObjectInputStream(fileIn);
			    MainFrame.this.dispatchEvent(new WindowEvent(MainFrame.this,
									     WindowEvent.WINDOW_CLOSING));
			    System.out.println(objIn.toString());			        
			    StockExchange stockExchange = (StockExchange) objIn.readObject();
			    MainFrame mf = new MainFrame(stockExchange, stockExchange.getTrader());
		            
			    objIn.close();			       
		        }
			catch (FileNotFoundException fnfex) {System.out.println(fnfex.getMessage());}
			catch (IOException ioex) {System.out.println(ioex.getMessage());}
			catch (ClassNotFoundException cfex) {System.out.println(cfex.getMessage());}
			
			JOptionPane.showMessageDialog(null, file.toString()
						      + " was loaded successfully.");
		    }
		}}
	    });
	
	loadItem.setActionCommand("LOAD_FILE");
        loadItem.setMnemonic(KeyEvent.VK_E);
        loadItem.setToolTipText("Load file");
        fileSection.add(loadItem);
	
        JRadioButtonMenuItem saveButton = new JRadioButtonMenuItem();
	loadButton.setActionCommand("SAVE_FILE");
	saveButton.setSelected(true);
        buttonsGroup.add(saveButton);

	/**
	 * If the option "Save file" is selected the user can choose to
	 * save the current session as a possible new starting point for the
	 * stock exchange.
	 */
        JMenuItem saveItem = new JMenuItem("Save file");
	saveItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JFileChooser fileChooser = new JFileChooser("./");
		    int option = fileChooser.showSaveDialog(MainFrame.this);
		    if(option == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			try {
			    FileOutputStream fileOut = new FileOutputStream(file);
			    ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			    objOut.writeObject(stockEx);
			    objOut.flush();
			    objOut.close();					    
			} catch (FileNotFoundException fnfex) {
			    System.out.println(fnfex.getMessage());
			} catch (IOException ioex) {
			    System.out.println(ioex.getMessage());
			}
			JOptionPane.showMessageDialog(null, file.toString()
						      + " was saved successfully.");
		    }}
	    });
	saveItem.setActionCommand("SAVE_FILE");
        saveItem.setMnemonic(KeyEvent.VK_E);
        saveItem.setToolTipText("Save file");
        fileSection.add(saveItem);
	
	topBar.add(fileSection);
	topBar.add(quit);
	topBar.add(newDaySection);
	
	companyPanel = new CompanyPanel(stockEx);
	tradingPanel = new TradingPanel(stockEx, companyPanel);
	predictionPanel = new PredictionPanel(stockEx);
	accountPanel = new AccountPanel(stockEx);
	
	setResizable(true);
        //setSize(new Dimension(700, 800));
        setLayout(new GridLayout(2, 2));

        tradingPanel.setUser(trader);
	tradingPanel.connectAccountPanel(accountPanel);
	
	setJMenuBar(topBar);
        add(companyPanel);
	add(tradingPanel);
	add(accountPanel);
	add(predictionPanel);
        
	pack();
	setResizable(true);
	setVisible(true);
    }
    public void loadStockExchange(StockExchange stockEx) {
	this.stockEx = stockEx;
    }
}
