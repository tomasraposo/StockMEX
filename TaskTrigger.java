import java.util.*;
import java.util.Timer.*;
//import javax.swing.Timer;
import java.util.TimerTask.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date.*;
import java.io.*;

public class TaskTrigger extends Timer {
    public TaskTrigger() {
	super();
	this.scheduleAtFixedRate(new Task(), 5000, 1000);
    }
    public void triggerAction(StockExchange stockEx, String ticker) {	
        try {
	    new Task().run();
	    stockEx.setActiveOrders();
	    Company company = Utils.lookupCompany(stockEx.getListedCompanies(), ticker);
	    stockEx.getTradingEngine().execute(company);
	} catch(Exception e) {
	    System.out.println("An error has ocurred with the order generation system.");
	}
    }
    class Task extends TimerTask {	
	@Override
	public void run() {
	    try {
	        Thread.sleep(1000);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
