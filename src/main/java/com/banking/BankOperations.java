package com.banking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.banking.database.DbConnection;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.operations.BankingOperations;

/**
 * Base implementation of backend bank service.
 */
public final class BankOperations implements BankingOperations {

	private static BankOperations instance = null;
	private Connection conn;


	private BankOperations() throws SQLException, ClassNotFoundException, IOException {
		this.conn = DbConnection.getConnection();
	};


	/**
	 * @return              BankOperations singleton
	 * @throws SQLException When database could not be connected to
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static BankOperations getInstance() throws SQLException, ClassNotFoundException, IOException {
		if ( instance == null ) {
			instance = new BankOperations();
		}

		return instance;
	}


	@Override
	public boolean createCustomer( Customer c ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Customer getCustomer( int customerId ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean createAccount( Account a ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Account getAccount( int customerId, long accountNumber ) {
		// TODO Auto-generated method stub
		return null;
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
