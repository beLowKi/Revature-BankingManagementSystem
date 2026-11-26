package com.banking.operations;

import java.util.List;

import com.banking.exception.BackendException;
import com.banking.exception.CustomerNotFoundException;
import com.banking.exception.InsufficientBalanceException;
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


	/**
	 * Creates a new Customer Account.
	 * 
	 * @param  a                    Incoming Account info
	 * @throws NullPointerException If argument is null
	 * @throws BackendException     For all other Exceptions
	 */
	void createAccount( final Account a ) throws NullPointerException, BackendException;


	/**
	 * Returns the full info of a Customer's Account.
	 * 
	 * @param  customerId       Target Customer ID
	 * @param  accountNumber    Target Account number
	 * @throws BackendException For all exceptions
	 */
	Account getAccount( int customerId, long accountNumber ) throws BackendException;


	/**
	 * Returns all Accounts with the given Customer ID.
	 * 
	 * @param  customerId               Target Customer ID
	 * @throws IllegalArgumentException if Customer ID isn't greater than 0
	 * @throws BackendException         For all other exceptions
	 */
	List<Account> getAccountsOf( int customerId ) throws IllegalArgumentException, BackendException;


	/**
	 * Returns all Accounts with the given Customer ID.
	 * 
	 * @param  c                    Customer whose accounts are returned
	 * @throws NullPointerException If given Customer is null
	 * @throws BackendException     For all other Exceptions
	 */
	List<Account> getAccountsOf( final Customer c ) throws NullPointerException, BackendException;


	/**
	 * Searches for the Account matching the given Customer ID and Account number
	 * and makes a deposit into it.
	 * 
	 * @param  customerId               Target Customer ID
	 * @param  accountNumber            Target Account number
	 * @param  amount                   Amount to deposit
	 * @throws IllegalArgumentException If deposit amount is less than 0
	 * @throws BackendException         For all exceptions
	 */
	void deposit(
		int customerId,
		long accountNumber,
		double amount
	) throws IllegalArgumentException, CustomerNotFoundException, BackendException;


	/**
	 * Makes a deposit into the given Account.
	 * 
	 * @param  a                        Target Account
	 * @param  amount                   Amount to deposit
	 * @throws NullPointerException     If provided account is null
	 * @throws IllegalArgumentException If deposit amount is less than 0
	 * @throws BackendException         For all otherexceptions
	 */
	void deposit(
		final Account a,
		double amount
	) throws NullPointerException, IllegalArgumentException, CustomerNotFoundException, BackendException;


	/**
	 * Searches for the Account matching the given Customer ID and Account number
	 * and makes a withdrawal.
	 * 
	 * @param  customerId                   Target Customer ID
	 * @param  accountNumber                Target Account number
	 * @param  amount                       Amount to withdraw
	 * @throws InsufficientBalanceException If target account has less balance than
	 *                                      the withdrawal amount
	 * @throws BackendException             For all other Exceptions
	 */
	void withdraw(
		int customerId,
		long accountNumber,
		double amount
	) throws InsufficientBalanceException, CustomerNotFoundException, BackendException;


	/**
	 * Makes a withdrawal into the given Account.
	 * 
	 * @param  a                            Target Account
	 * @param  amount                       Amount to withdraw
	 * @throws NullPointerException         If given Account is null
	 * @throws InsufficientBalanceException If target account has less balance than
	 *                                      the withdrawal amount
	 * @throws BackendException             For all other Exceptions
	 */
	void withdraw(
		final Account a,
		double amount
	) throws NullPointerException, InsufficientBalanceException, CustomerNotFoundException, BackendException;


	/**
	 * Searches for 2 Accounts and makes a transfer from the former to the latter.
	 * The entire process fails, preventing any change to either account, if 1) one
	 * or more accounts do not exist 2) "from" account has insufficient funds or 3)
	 * an unexpected error occurs during the transfer.
	 * 
	 * @param  fromCustomerId               Customer ID of the sending Account
	 * @param  fromAccountNumber            number of the sending Account
	 * @param  toCustomerId                 Customer ID of the receiving Account
	 * @param  toAccountNumber              number of receiving Account
	 * @param  amount                       Amount to transfer
	 * @throws InsufficientBalanceException If sending Account does not have enough
	 *                                      funds to transfer
	 * @throws IllegalArgumentException     If transfer amount is not greater than 0
	 * @throws BackendException             For all other Exceptions
	 */
	void transfer(
		int fromCustomerId,
		long fromAccountNumber,
		int toCustomerId,
		long toAccountNumber,
		double amount
	) throws InsufficientBalanceException, IllegalArgumentException, BackendException;


	/**
	 * Transfers funds from one Account to another. The entire process fails,
	 * preventing any change to either account, if 1) one or more accounts do not
	 * exist 2) "from" account has insufficient funds or 3) an unexpected error
	 * occurs during the transfer.
	 * 
	 * @param  from
	 * @param  to
	 * @param  amount                       Amount to transfer
	 * @throws InsufficientBalanceException If sending Account does not have enough
	 *                                      funds to transfer
	 * @throws NullPointerException         If either Account is null
	 * @throws IllegalArgumentException     If transfer amount is not greater than 0
	 * @throws BackendException             For all other Exceptions
	 */
	void transfer(
		Account from,
		Account to,
		double amount
	) throws InsufficientBalanceException, NullPointerException, IllegalArgumentException, BackendException;


	/**
	 * Returns all Transactions associated with the given Customer ID. May return an
	 * empty list.
	 * 
	 * @param  customerId       Target Customer ID
	 * @throws BackendException For all exceptions
	 */
	List<Transaction> getTransactionHistory( int customerId ) throws BackendException;


	/**
	 * Returns all of the given Customer's Transactions. May return an empty list
	 * 
	 * @param  c                    Target Customer
	 * @throws NullPointerException If given Customer is null
	 * @throws BackendException     For all other Exceptions
	 */
	List<Transaction> getTransactionHistory( final Customer c ) throws NullPointerException, BackendException;

}
