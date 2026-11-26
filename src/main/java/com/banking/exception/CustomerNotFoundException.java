package com.banking.exception;

/**
 * This is thrown by operations that failed to retrieve
 * the Customer info that they rely on such as viewing and 
 * selecting accounts to make a deposit.
 */
public class CustomerNotFoundException extends Exception {

	private static final long serialVersionUID = 4219745582301154960L;

	public CustomerNotFoundException( String message ) {
		super( message );
	}

}
