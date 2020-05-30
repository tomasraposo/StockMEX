import java.util.*;
import java.text.DecimalFormat;
import java.util.stream.*;
import java.io.Serializable;

/**
 * One the child classes of Share.
   This class provides implementation 
   for all of Share class' abstract methods.
 */

public final class UnlistedShare extends Share implements Serializable {

    private double volatility;
    private double dividend;
    private boolean protectedStatus;
    
    public UnlistedShare(double currentPrice,
			 double lastPrice,
			 double nominalValue,
			 double earningsPerShare,
			 double dividend) {

	super(currentPrice,
	      lastPrice,
	      nominalValue,
	      earningsPerShare);

	volatility = new Random().nextDouble() * 0.7;
        this.dividend = dividend;
	protectedStatus = false;
    }
    @Override
    public void setVolatility() {
	volatility = new Random().nextDouble() * 0.7;
    }
    public String getProtectedStatus() {
	return String.valueOf(protectedStatus);
    }
    @Override
    public double getVolatility() {
	return volatility;
    }
    public double getDividendPriceRatio() {
	return dividend / super.getCurrentPrice();
    }
    @Override
    public String getDescriptor() {
	return ShareDescriptor.unlistedShare;
    }
    @Override
    public String toString() {
	return super.toString() + "\n The Dividend price ratio is " + getDividendPriceRatio();
    }
}
