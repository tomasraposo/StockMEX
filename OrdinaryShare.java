import java.util.*;
import java.text.DecimalFormat;
import java.util.stream.*;
import java.io.Serializable;

/**
 * One the child classes of Share.
   This class provides implementation 
   for all of Share class' abstract methods.
 */

public final class OrdinaryShare extends Share implements Serializable {

    private double dividend;
    private boolean votingRights;
    private double volatility;
    /*
      It has two distinct constructors.
      The former initializes a new Ordinary Share.
      The latter is a copy constructor so as to
      avoiding using .clone().
     */
    public OrdinaryShare(double currentPrice,
			 double lastPrice,
			 double nominalValue,
			 double dividend,
			 double earningsPerShare) {
	super(currentPrice,
	      lastPrice,
	      nominalValue,
              earningsPerShare);

	votingRights = true;
    }
    public OrdinaryShare(OrdinaryShare share) {
	this(share.getCurrentPrice(),
	     share.getLastPrice(),
	     share.getNominalValue(),
	     share.getDividend(),
	     share.getEarningsPerShare());
	votingRights = true;
    }
    public void setDividend(double dividend) {
	this.dividend = dividend;
    }
    @Override
    public void setVolatility() {
	volatility = new Random().nextDouble() * 0.4;
    }
    public double getDividend() {
	return dividend;
    }
    @Override
    public double getVolatility() {
	return volatility;
    }
    public String getVotingRights() {
	return String.valueOf(votingRights);
    }
    @Override
    public double getDividendPriceRatio() {
	return (dividend / super.getCurrentPrice());
    }
    @Override
    public String getDescriptor() {
	return ShareDescriptor.ordinaryShare;
    }
    @Override
    public String toString() {
	return super.toString() + "\nThe Dividend price ratio is " + getDividendPriceRatio();
    }
}
