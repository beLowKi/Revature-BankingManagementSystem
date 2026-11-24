package com.banking.operations;

import java.util.List;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;

/**
 * Defines backend banking operations
 */
public interface BankingOperations {

	boolean createCustomer( final Customer c );


	Customer getCustomer( int customerId );


	boolean createAccount( final Account a );


	Account getAccount( int customerId, long accountNumber );


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
