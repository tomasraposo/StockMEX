import java.util.*;
import java.text.DecimalFormat;
import java.util.stream.*;
import java.io.Serializable;

/**
 * One the child classes of Share.
   This class provides implementation 
   for all of Share class' abstract methods.
 */
public final class PreferredShare extends Share implements Serializable{

    private double volatility;
    private double fixedDividend;    

    /*
      It has two distinct constructors.
      The former initializes a new Preferred Share.
      The latter is a copy constructor so as to
      avoiding using .clone().
     */
    public PreferredShare(double currentPrice,
			  double lastPrice,
			  double nominalValue,
			  double earningsPerShare,
			  double fixedDividend) {

	super(currentPrice,
	      lastPrice,
	      nominalValue,
	      earningsPerShare);

	volatility = new Random().nextDouble() * 0.2;
	this.fixedDividend = fixedDividend;
    }
    public PreferredShare(PreferredShare share) {
	this(share.getCurrentPrice(),
	     share.getLastPrice(),
	     share.getNominalValue(),
	     share.getEarningsPerShare(),
	     share.getFixedDividend());	
    }
    @Override
    public void setVolatility() {
	volatility = new Random().nextDouble() * 0.2;
    }
    public void setFixedDividend(double fixedDividend) {
	this.fixedDividend = fixedDividend;
    }
    @Override
    public double getVolatility() {
	return volatility;
    }
    public double getFixedDividend() {
	return fixedDividend;
    }
    @Override
    public double getDividendPriceRatio() {
	return (fixedDividend * super.getNominalValue()) / (100 * super.getCurrentPrice());
    }
    @Override
    public String getDescriptor() {
	return ShareDescriptor.preferredShare;
    }
    @Override
    public String toString() {
	return super.toString() + "\nFixed dividend agreed at: " + getFixedDividend()
	    + "\nThe Dividend Price ratio is " + getDividendPriceRatio();	
    }
}
