package ch.uzh.marugoto.core.data.entity;

/**
 * Represents virtual game money.
 * 
 * Negative or positive amount values can be represented by a signed value
 * (+/-).
 * 
 * If absolute, the account will be reset to the given amount, otherwise the
 * given amount value will be subtracted/added to the current account value.
 */
public class Money {
	private double amount = 0.0;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Money() {
		super();
	}

	public Money(double amount) {
		this();
		this.amount = amount;
	}
}
