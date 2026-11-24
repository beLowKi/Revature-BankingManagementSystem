package com.banking.model;

import java.sql.Date;
import java.time.LocalDate;

import com.banking.enums.AccountType;

/**
 * Represents one of a Customer's account.
 */
public class Account {

	private long number;
	private int customerId;
	private AccountType accountType;
	private double balance;
	private Date date;

	// Default constructor
	public Account() {}


	/**
	 * Constructs a new Account. Date is automatically set to today.
	 * 
	 * @param customerId  Customer ID
	 * @param accountType Type of the Account
	 * @param balance     Dollar amount
	 */
	public Account( int customerId, AccountType accountType, double balance ) {
		this.customerId = customerId;
		this.accountType = accountType;
		this.balance = balance;
		this.date = Date.valueOf( LocalDate.now() );
	}


	/**
	 * @param customerId  Customer ID
	 * @param accountType Type of the Account
	 * @param balance     Dollar amount
	 * @param date        Creation date
	 */
	public Account( int customerId, AccountType accountType, double balance, Date date ) {
		this( customerId, accountType, balance );
		this.date = date;
	}


	/**
	 * @return the number
	 */
	public long getNumber() { return number; }


	/**
	 * @return the customerId
	 */
	public int getCustomerId() { return customerId; }


	/**
	 * @return the accountType
	 */
	public AccountType getAccountType() { return accountType; }


	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType( AccountType accountType ) { this.accountType = accountType; }


	/**
	 * @return the balance
	 */
	public double getBalance() { return balance; }


	/**
	 * @param balance the balance to set
	 */
	public void setBalance( double balance ) { this.balance = balance; }


	/**
	 * @return the date
	 */
	public Date getDate() { return date; }


	/**
	 * @param date the date to set
	 */
	public void setDate( Date date ) { this.date = date; }

}
