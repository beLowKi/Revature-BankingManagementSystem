package com.banking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.banking.database.DbConnection;
import com.banking.enums.AccountType;
import com.banking.exception.BackendException;
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

			// Converting returned row into a Customer object
			// TBD instead of asserts, which would throw meaningless (to the user)
			// AssertionError, use a custom Exception. However, Error may be
			// more appropriate because if the wrong customer ID or an
			// account type with an invalid value are returned, then something
			// is severely wrong with the database's state, and there's nothing
			// that they, the user, could do about it. By definition, that sounds
			// more like an Error than an Exception since the latter implies
			// that the user should respond in some way.
			//
			// ALSO I could make custom Errors. In fact, some uses
			// of BackendException should probably be a BackendError?
			// Things like failing to connect to the database, for example.
			final long number = result.getLong( "number" );
			final int actualCustomerId = result.getInt( "customer_id" );
			assert customerId == actualCustomerId;

			final AccountType type = AccountType.valueOf( result.getString( "type" ) );
			assert type != null;

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
	public boolean deposit( int customerId, long accountNumber, double amount ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean deposit( Account a, double amount ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean withdraw( int customerId, long accountNumber, double amount ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean withdraw( Account a, double amount ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean transfer( Account from, Account to, double amount ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean transfer(
		int fromCustomerId,
		long fromAccountNumber,
		int toCustomerId,
		long toAccountNumber,
		double amount
	) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public List<Transaction> getTransactionHistory( int customerId ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Transaction> getTransactionHistory( Customer c ) {
		// TODO Auto-generated method stub
		return null;
	}

}
