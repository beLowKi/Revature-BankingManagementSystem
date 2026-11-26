package com.banking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.banking.database.DbConnection;
import com.banking.enums.AccountType;
import com.banking.exception.BackendException;
import com.banking.exception.CustomerNotFoundException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.operations.BankingOperations;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

/**
 * Base implementation of backend bank service.
 */
public final class BankOperations implements BankingOperations {

	private static BankOperations instance = null;


	private BankOperations() throws BackendException {

		// Initializes database tables
		this.init();

	};


	/**
	 * @return                  BankOperations singleton
	 * @throws BackendException If any exceptions occur
	 */
	public static BankOperations getInstance() throws BackendException {
		if ( instance == null ) {
			instance = new BankOperations();
		}

		return instance;
	}


	// Initializes model tables in database
	private void init() throws BackendException {

		// SQL Query to create customers table
		// @formatter:off
		final String createCustomersStr = (
			"CREATE TABLE IF NOT EXISTS " + Customer.TABLE + " (\n" +
				"\tid 		INTEGER PRIMARY KEY AUTO_INCREMENT, \n" +
				"\tname 	VARCHAR(255) NOT NULL, \n" +
				"\temail 	VARCHAR(255) UNIQUE NOT NULL, \n" +
				"\tphone 	VARCHAR(15) UNIQUE NOT NULL \n" +
			");"
		);
		
		// SQL Query to create accounts table
		final String createAccountsStr = (
			"CREATE TABLE IF NOT EXISTS " + Account.TABLE + " (\n" +
				"\tnumber 			BIGINT PRIMARY KEY AUTO_INCREMENT, \n" +
				"\tcustomer_id 		INTEGER, \n" +
				"\taccount_type 	VARCHAR(15), \n" +
				"\tbalance 			DECIMAL(10, 2), \n" +
				"\tdate 			DATE, \n" +			
				"\tFOREIGN KEY (customer_id) REFERENCES customers (id) \n" +
				"\tON DELETE CASCADE \n" +
				"\tON UPDATE CASCADE \n" +
			");"
		);
		
		// TODO CREATE statements for other tables
		
		// TODO CREATE practical indexes
		
		// TODO CREATE practical constraints like balance >= 0.00
		
		// @formatter:on

		// Executing statements
		try (
			Connection conn = DbConnection.get();
			PreparedStatement createCustomers = conn.prepareStatement( createCustomersStr );
			PreparedStatement createAccounts = conn.prepareStatement( createAccountsStr );
		) {

			createCustomers.executeUpdate();
			createAccounts.executeUpdate();

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Initialization Error" );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public void createCustomer( Customer c ) throws NullPointerException, BackendException {

		// Checking if Customer is null
		if ( c == null ) {
			throw new NullPointerException( "Customer must not be null" );
		}

		// SQL Query
		// @formatter:off
		final String sqlString = ( 
			"INSERT INTO " + Customer.TABLE + " (name, email, phone) VALUES \n" +
				"( \"" + c.getName() + "\", \"" + c.getEmail() + "\", \"" + c.getPhone() + "\");"
		);
		// @formatter:on

		// Executing query
		try ( Connection conn = DbConnection.get(); PreparedStatement stmt = conn.prepareStatement( sqlString ) ) {

			// 'false' is returned because query was successfully
			// sent to the database but, for some reason, failed to
			// add a row to the customers table
			if ( stmt.executeUpdate() != 1 ) {
				throw new BackendException( "Failed to insert data" );
			}

		} catch ( BackendException e ) {
			throw e;

			// This exception is thrown when VARCHARs, like all of Customer's String
			// fields, exceed their character limit.
		} catch ( MysqlDataTruncation e ) {
			BackendException be = new BackendException( "Invalid string input; " + e.getMessage() );
			be.addSuppressed( e );
			throw be;

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Error creating Customer: " + e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public Customer getCustomer( int customerId ) throws BackendException {

		// Catching negative IDs before touching database
		if ( customerId < 0 ) {
			return null;
		}

		// SQL Query
		final String sqlString = "SELECT * FROM " + Customer.TABLE + " WHERE id = " + customerId + " LIMIT 1;";

		// Executing query
		try ( Connection conn = DbConnection.get(); PreparedStatement stmt = conn.prepareStatement( sqlString ) ) {

			ResultSet result = stmt.executeQuery();
			if ( !result.next() ) {
				return null;
			}

			// Converting returned row into a Customer object
			final int id = result.getInt( "id" );
			final String name = result.getString( "name" );
			final String email = result.getString( "email" );
			final String phone = result.getString( "phone" );

			return new Customer( id, name, email, phone );

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Error getting Customer: " + e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public void createAccount( Account a ) throws BackendException {

		// Checking for null input
		if ( a == null ) {
			throw new NullPointerException( "Account cannot be null" );
		}

		// SQL query
		// @formatter:off
		final String sqlString = (
			"INSERT INTO " + Account.TABLE + " (customer_id, account_type, balance, date)" +
			" VALUES (" + a.getCustomerId() + ", \"" + a.getAccountType().getValue() + "\", " +
			a.getBalance() + ", \"" + a.getDate() + "\");"
		);
		// @formatter:on

		// Executing query
		try ( Connection conn = DbConnection.get(); PreparedStatement stmt = conn.prepareStatement( sqlString ) ) {

			// 'false' is returned because query was successfully
			// sent to the database but, for some reason, failed to
			// add a row to the customers table
			if ( stmt.executeUpdate() != 1 ) {
				throw new BackendException( "Failed to store data" );
			}

		} catch ( BackendException e ) {
			throw e;

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Error creating Account: " + e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public Account getAccount( int customerId, long accountNumber ) throws BackendException {

		// Catching negative IDs before touching database
		if ( customerId < 0 || accountNumber < 0 ) {
			return null;
		}

		// SQL Query
		final String sqlString =
			"SELECT * FROM " + Account.TABLE +
				" WHERE number = " + accountNumber + " AND" +
				" customer_id = " + customerId +
				" LIMIT 1;";

		// Executing query
		try (
			Connection conn = DbConnection.get();
			PreparedStatement stmt = conn.prepareStatement( sqlString )
		) {

			ResultSet result = stmt.executeQuery();
			if ( !result.next() ) {
				return null;
			}

			// Loading Account 
			final long number = result.getLong( "number" );
			final int actualCustomerId = result.getInt( "customer_id" );
			final AccountType type = AccountType.valueOf( result.getString( "account_type" ) );
			final double balance = result.getDouble( "balance" );
			final Date date = result.getDate( "date" );

			return new Account( number, actualCustomerId, type, balance, date );

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Error getting Account: " + e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public List<Account> getAccountsOf( int customerId ) throws IllegalArgumentException, BackendException {

		// Catching invalid arguments before touching the database
		if ( customerId <= 0 ) {
			throw new IllegalArgumentException( "Customer ID must be greater than 0" );
		}

		// SQL Query
		final String sqlString = "SELECT * FROM " + Account.TABLE + " WHERE customer_id = " + customerId;

		// Executing statement
		try (
			Connection conn = DbConnection.get();
			PreparedStatement stmt = conn.prepareStatement( sqlString )
		) {

			ResultSet results = stmt.executeQuery();
			List<Account> accounts = new ArrayList<Account>();

			// Extracting Accounts
			while ( results.next() ) {

				final long number = results.getLong( "number" );
				final AccountType type = AccountType.valueOf( results.getString( "account_type" ) );
				final double balance = results.getDouble( "balance" );
				final Date date = results.getDate( "date" );

				accounts.add( new Account( number, customerId, type, balance, date ) );

			}

			return accounts;

		} catch ( Exception e ) {
			BackendException be = new BackendException( e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public List<Account> getAccountsOf( Customer c ) throws NullPointerException, BackendException {
		return getAccountsOf( c.getId() );
	}


	@Override
	public void deposit(
		int customerId,
		long accountNumber,
		double amount
	) throws IllegalArgumentException, CustomerNotFoundException, BackendException {

		// Catching illegal arguments before touching database
		if ( customerId <= 0 ) {
			throw new IllegalArgumentException( "Customer ID must be greater than 0" );
		}
		if ( accountNumber <= 0 ) {
			throw new IllegalArgumentException( "Account number must be greater than 0" );
		}
		if ( amount <= 0.0 ) {
			throw new IllegalArgumentException( "Deposit amount must be greater than 0" );
		}

		// Checking for existing Customer
		if ( getCustomer( customerId ) == null ) {
			throw new CustomerNotFoundException( "Customer with ID " + customerId + " not found" );
		}

		// SQL Query
		final String sqlString =
			"UPDATE " + Account.TABLE +
				" SET balance = balance + " + amount +
				" WHERE number = " + accountNumber + " AND" +
				" customer_id = " + customerId + ";";

		// Executing statement
		try (
			Connection conn = DbConnection.get();
			PreparedStatement stmt = conn.prepareStatement( sqlString )
		) {

			if ( stmt.executeUpdate() != 1 ) {
				throw new BackendException( "Failed to update Account balance" );
			}

		} catch ( BackendException e ) {
			throw e;

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Error making deposit: " + e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public void deposit(
		Account a,
		double amount
	) throws NullPointerException, IllegalArgumentException, BackendException, CustomerNotFoundException {
		deposit( a.getCustomerId(), a.getNumber(), amount );
	}


	@Override
	public void withdraw(
		int customerId,
		long accountNumber,
		double amount
	) throws InsufficientBalanceException, BackendException, CustomerNotFoundException {

		// Catching illegal arguments before touching database
		if ( customerId <= 0 ) {
			throw new IllegalArgumentException( "Customer ID must be greater than 0" );
		}
		if ( accountNumber <= 0 ) {
			throw new IllegalArgumentException( "Account number must be greater than 0" );
		}
		if ( amount <= 0.0 ) {
			throw new IllegalArgumentException( "Withdrawal amount must be greater than 0" );
		}

		// Checking for existing Customer
		if ( getCustomer( customerId ) == null ) {
			throw new CustomerNotFoundException( "Customer with ID " + customerId + " not found" );
		}

		// Checking Account balance to ensure it
		// has enough funds to withdraw
		Account acc = getAccount( customerId, accountNumber );
		if ( acc.getBalance() < amount ) {
			throw new InsufficientBalanceException( "Withdrawal denied; insufficient funds" );
		}

		// SQL Query
		final String sqlString =
			"UPDATE " + Account.TABLE +
				" SET balance = balance - " + amount +
				" WHERE number = " + accountNumber + " AND" +
				" customer_id = " + customerId + ";";

		// Executing statement
		try (
			Connection conn = DbConnection.get();
			PreparedStatement stmt = conn.prepareStatement( sqlString )
		) {

			if ( stmt.executeUpdate() != 1 ) {
				throw new BackendException( "Failed to update Account balance" );
			}

		} catch ( BackendException e ) {
			throw e;

		} catch ( Exception e ) {
			BackendException be = new BackendException( "Error making deposit: " + e.getMessage() );
			be.addSuppressed( e );
			throw be;
		}

	}


	@Override
	public void withdraw(
		Account a,
		double amount
	) throws NullPointerException, InsufficientBalanceException, BackendException, CustomerNotFoundException {
		withdraw( a.getCustomerId(), a.getNumber(), amount );
	}


	@Override
	public void transfer(
		int fromCustomerId,
		long fromAccountNumber,
		int toCustomerId,
		long toAccountNumber,
		double amount
	) throws InsufficientBalanceException, IllegalArgumentException, BackendException {
		// TODO Auto-generated method stub

	}


	@Override
	public void transfer(
		Account from,
		Account to,
		double amount
	) throws InsufficientBalanceException, NullPointerException, IllegalArgumentException, BackendException {
		// TODO Auto-generated method stub

	}


	@Override
	public List<Transaction> getTransactionHistory( int customerId ) throws BackendException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Transaction> getTransactionHistory( Customer c ) throws NullPointerException, BackendException {
		// TODO Auto-generated method stub
		return null;
	}

}
