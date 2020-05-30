import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.stream.*;
import java.awt.event.*;
import java.awt.font.*;

public class PlotPanel extends JPanel implements ActionListener {    

    private StockExchange stockEx;
    private int maxWidth;
    private int maxHeight;
    private JButton estimate;
    private ArrayList<Double> data;
    private ArrayList<Point> dataPoints;
    
    public PlotPanel(StockExchange stockEx) {
	super();
	this.stockEx = stockEx;
	setLayout(new BorderLayout(5, 5));
	setPreferredSize(new java.awt.Dimension(450, 450));
        data = new ArrayList<>(Arrays.asList(new DataGenerator().dataGen()));
	estimate = new JButton("View Price Change Rate");
	estimate.setActionCommand("ESTIMATE_PRICES");	
	estimate.addActionListener(this);

	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
	buttonPanel.add(estimate);
	JButton savePlot = new JButton();
        
	add(buttonPanel, BorderLayout.PAGE_START);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("ESTIMATE_PRICES")) {
	    data=new ArrayList<>(Arrays.asList(new DataGenerator().dataGen()));
	    updateUI();
	    repaint();
	}
    }
    public ArrayList<Double> getData() {
	return data;
    }
    public void setData(java.util.List<Double> data) {
	this.data=(ArrayList<Double>)data;
    }
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	
	maxWidth = super.getWidth();
	maxHeight = super.getHeight();
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

	double xScale = (double) ((maxWidth - 75) / (data.size() - 1));
        double yScale = (double) ((maxHeight - 75) / (Collections.max(data) - Collections.min(data)));
	
	dataPoints = new ArrayList<>(){{
		for (int i = 0; i < data.size(); i++)
		    this.add(new Point((int)(i *xScale + 50),
				       (int) ((Collections.max(data) - data.get(i)) * yScale + 25)));
	    }};	
	drawXYAxis(g2);
	
        g2.setColor(Color.RED);
	dataPoints.stream().limit(dataPoints.size() - 1)
	    .forEach(s -> g2.draw(new Line2D.Double(s.x,s.y,
						    dataPoints.get(dataPoints.indexOf(s) + 1).x,
						    dataPoints.get(dataPoints.indexOf(s) + 1).y)));
	g2.setColor(Color.BLUE);
	dataPoints.stream().forEach(s -> g2.fillRect(s.x-2,s.y-2,4, 4));
    }
    private void drawXYAxis(Graphics2D g2) {
	//y axis
	g2.draw(new Line2D.Double(50, maxHeight - 50, 50, 25));
	//x axis
	g2.draw(new Line2D.Double(50, maxHeight - 50, maxWidth - 25, maxHeight - 50));
    }
    private class DataGenerator {    
	public DataGenerator() {}
        public Double[] dataGen() {
	    Double[] arrayData = new Double[100];
	    for (int i = 0; i < arrayData.length; i++) {
		arrayData[i] =(Double)(new Random()).nextDouble() * i;
	    }
	    return arrayData;
	}
    }    
}
