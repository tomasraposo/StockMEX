import java.util.*;
import java.io.Serializable;

/**
 * Child class of Account class.
 * This is an account suitable for trading purposes.
 */
public final class InvestmentAccount extends Account implements Serializable {

    private String accountCode;
    private double transactionFee;
    private double fundManagerFee;
    private double customerFee;

    /**
     * Again, two constructors
     * that follow the same principles
     * as in the Account class.
     */
    public InvestmentAccount() {
	super();
	accountCode = accountCodeGenerator() ;
	transactionFee = feeGenerator(0.2);
	fundManagerFee = feeGenerator(0.3);
	customerFee = feeGenerator(0.4);
    }
    public InvestmentAccount(double balance, double interest) {	
	super(balance, interest);
	accountCode = accountCodeGenerator() ;
	transactionFee = feeGenerator(0.02);
	fundManagerFee = feeGenerator(0.03);
	customerFee = feeGenerator(0.004);	
    }
    /**
     * Getters and setters for retrieving and 
     * mutating each instance variable value
     */
    public void setTransactionFee(double transactionFee) {
	this.transactionFee = transactionFee;
    }
    public void setFundManagerFee(double fundManagerFee) {
	this.fundManagerFee = fundManagerFee;
    }
    public void setCustomerFee(double customerFee) {
	this.customerFee = customerFee;
    }
    public String getAccountCode() {
	return accountCode;
    }
    public double getTransactionFee() {
	return transactionFee;
    }
    public double getFundManagerFee() {
	return fundManagerFee;
    }
    public double getCustomerFee() {
	return customerFee;
    }
    public void deductFeesFromAccountBalance() {
	double jointFees = transactionFee + fundManagerFee + customerFee;
	super.setBalance(super.getBalance() - (super.getBalance() * (jointFees)));
    }
    @Override
    public String toString() {
	return super.toString()
	    + String.format
	    (".%nTransaction Fee: %.3f%nFund Manager Fee: %.3f%nCustomer Fee: %.3f%n",
	     transactionFee, fundManagerFee, customerFee); 
    }
    public static double feeGenerator(double inRange) {
	return new Random().nextDouble() * inRange;
    }
    /**
     * Generates an unique account code
     */
    public static String accountCodeGenerator() {
	StringBuilder strBuilder = new StringBuilder(10);
	for (int i = 0; i < 10; i++) {
	    if (i < 4) {
		strBuilder.append((char)(new Random().nextInt(26) + 65));
	    } else if (i % 2 == 0) {
		strBuilder.append("-");
	    } else {
		strBuilder.append(new Random().nextInt(100));
	    }
	}
	return strBuilder.toString();
    }
}

