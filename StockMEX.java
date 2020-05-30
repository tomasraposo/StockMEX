import java.awt.*;      
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.metal.*;
import java.awt.Toolkit;
import java.io.*;
import java.io.Serializable;
/**
 ************* PROGRAM STARTS HERE ********************
 * This is the main class where program execution starts.
 * It has a stock exchange, a trader and the actual GUI
 * as its instance/member variables.
 ******************************************************/
public class StockMEX implements Serializable  {
    private StockExchange stockEx;
    private MainFrame dataFrame;    
    private Trader trader;

    public StockMEX() {
        stockEx = new StockExchange();
	trader = new Trader("user1");
        dataFrame = new MainFrame(stockEx, trader);	
    }
    public static void main(String[] args) {
	try {
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    Toolkit.getDefaultToolkit().setDynamicLayout(true);
	    System.setProperty("sun.awt.noerasebackground", "true");
	    UIManager.setLookAndFeel(new MetalLookAndFeel());
	    MetalLookAndFeel.setCurrentTheme(new OceanTheme());
	} catch (UnsupportedLookAndFeelException ex) {
	    ex.printStackTrace();
	}
	SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		    new StockMEX();
		}
	    });
    }
}



