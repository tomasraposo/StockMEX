import java.io.Serializable;

/**
 * Account class
 * This is a generic kind of account.
 * Parent class of InvestmentAccount class.
 */

public class Account implements Serializable {

    private double balance;
    private double interest;
    /**
     * Two constructors:
     The former creates a new account.
     The latter creates a new account and initializes its
     fields to the values passed as arguments.
     */
    public Account() {
	balance = 0;
	interest = 0;
    }
    public Account(double balance, double interest) {
	this.balance = balance;
	this.interest = interest;
    }
    /**
     * Getters and setters for retrieving
     * and mutating each instance variable value.
     */
    public double getBalance() {
	return balance;
    }
    public double getInterest() {
	return interest;
    }
    public void setBalance(double amount) {
	balance = amount;
    }
    public void addMoneyToBalance(double amount) {
	balance += amount;
    }
    public void takeMoneyFromBalance(double amount) {
	balance -= amount;
    }
    public void setInterest(double rate) {
	interest = rate;
    }
    public int calculateInterest(int n) {
	return (int) (balance * (Math.pow(1+interest, n/12)));
    }
    public void printAccountStatus(int months) {
	System.out.println("Over a period of " + months
			   + " months the corresponding cumulative interest is "
			   + calculateInterest(months));
    }
    public String toString() {
	return String.format("The current balance is %.3f with an interest rate of %.1f",
			     balance, interest);
    }
}
