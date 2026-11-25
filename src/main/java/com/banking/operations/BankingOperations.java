package com.banking.operations;

import java.util.List;

import com.banking.exception.BackendException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;

/**
 * Defines backend banking operations
 */
public interface BankingOperations {

	/**
	 * Creates a new Customer.
	 * 
	 * @param  c                    Incoming Customer data
	 * @throws NullPointerException If provided Customer is null
	 */
	void createCustomer( final Customer c ) throws NullPointerException, BackendException;


	/**
	 * Returns the full info for the Customer with the given ID or {@code null} if
	 * no match is found.
	 * 
	 * @param  customerId Target Customer ID
	 * @return            Customer data or null on ID miss
	 */
	Customer getCustomer( int customerId ) throws BackendException;


	void createAccount( final Account a ) throws NullPointerException, BackendException;


	Account getAccount( int customerId, long accountNumber ) throws BackendException;


	boolean deposit( int customerId, long accountNumber, double amount );


	boolean deposit( final Account a, double amount );


	boolean withdraw( int customerId, long accountNumber, double amount );


	boolean withdraw( final Account a, double amount );


	boolean transfer( Account from, Account to, double amount );


	boolean transfer(
		int fromCustomerId,
		long fromAccountNumber,
		int toCustomerId,
		long toAccountNumber,
		double amount
	);


	List<Transaction> getTransactionHistory( int customerId );


	List<Transaction> getTransactionHistory( final Customer c );

}
