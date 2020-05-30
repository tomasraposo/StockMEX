import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class handles the stock price estimation rates
 * display
 */
@SuppressWarnings("serial")
public final class PredictionPanel extends JPanel {

    private StockExchange stockEx;
    private PlotPanel plotPanel;
    private JLabel predictionsLabel;
    private JTextField timeField;
    private JScrollPane scrollPane;
    private JTextArea displayPredictions;
    private JButton predictionButton;
    
    public PredictionPanel(StockExchange stockEx) {
	super(new GridLayout(1,1));
        this.stockEx = stockEx;
	this.plotPanel = new PlotPanel(stockEx);

	setBorder(BorderFactory.createTitledBorder("Stock Price Estimation"));

	displayPredictions = new JTextArea(20,20);
        
	JLabel time = new JLabel("Time (in days)");

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel displayPlot = new JPanel();

	displayPlot.setBorder(BorderFactory.createTitledBorder("Historic price visualiser"));
        displayPlot.add(plotPanel);

	timeField = new JTextField(10);

	scrollPane = new JScrollPane(displayPredictions);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        
	predictionButton = new JButton();
	predictionButton.setActionCommand("PREDICT");
        predictionButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals("PREDICT")) {
			try {
			    displayStockPriceEstimation(Integer.parseInt(timeField.getText()));
			} catch  (NumberFormatException nfe) {
			    JOptionPane.showMessageDialog(null, "Only numbers are accepted");
			}
		    }
		}
	    });

	panel.add(predictionButton); //BorderLayout.EAST);
        panel.add(time);//, BorderLayout.PAGE_START);
	panel.add(timeField, BorderLayout.EAST);
	panel.add(scrollPane, BorderLayout.WEST);	
	add(panel);
	add(displayPlot);
    }
    /**
     * This method display an estimation of the current stock prices
     * of all listed companies across a user specified time frame in days.
     */
    public void displayStockPriceEstimation(int time) {
	for (int i = 0; i < time; i++) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_WEEK, i);
	    displayPredictions.append(new StringBuilder("\n"+calendar.getTime().toString()+"\n")
				       .append(":").toString());
	    for (Company company : stockEx.getListedCompanies()) {
		for (Stock stock : company.getCompanyStock()) {
		    displayPredictions.append(Utils.priceChangeEstimationToString(stock)+"\n");
		}
	    }
	}	    
    }
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
	     JFrame frame = new JFrame("StockMEX");
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.getContentPane().add(new PredictionPanel(new StockExchange()));
	     frame.pack();
	     frame.setLocationRelativeTo(null);
	     frame.setVisible(true);	     
	 }
      });
    }
}
