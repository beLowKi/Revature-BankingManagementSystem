package com.banking.model;

import java.sql.Timestamp;
import java.time.Instant;

import com.banking.enums.TransactionType;

/**
 * Represents a transaction from a Customer account.
 * 
 * @author Malcolm
 */
public class Transaction {	
	private int id;
	private long accountNumber;
	private TransactionType type;
	private double amount;
	private String reference;
	private Timestamp date;
	
	
	/**
	 * Constructs a new Transaction.
	 * Date is automatically set as today.
	 * 
	 * @param accountNumber Customer account ID
	 * @param type Type of the Transaction
	 * @param amount Dollar amount
	 * @param reference Grouping string
	 */
	public Transaction(long accountNumber, TransactionType type, double amount, String reference) {
		this.accountNumber = accountNumber;
		this.type = type;
		this.amount = amount;
		this.reference = reference;
		this.date = Timestamp.from(Instant.now());
	}

	
	/**
	 * @param accountNumber Customer account ID
	 * @param type Type of the Transaction
	 * @param amount Dollar amount
	 * @param reference Grouping string
	 * @param date Date which Transaction occurred
	 */
	public Transaction(
		long accountNumber, 
		TransactionType type, 
		double amount, 
		String reference, 
		Timestamp date
	) {
		this.accountNumber = accountNumber;
		this.type = type;
		this.amount = amount;
		this.reference = reference;
		this.date = date;
	}

	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the accountNumber
	 */
	public long getAccountNumber() {
		return accountNumber;
	}


	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}


	/**
	 * @return the type
	 */
	public TransactionType getType() {
		return type;
	}

	
	/**
	 * @param type the type to set
	 */
	public void setType(TransactionType type) {
		this.type = type;
	}


	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}


	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}


	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}


	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}


	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	
	
}
