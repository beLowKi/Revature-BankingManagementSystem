package com.banking.exception;

/**
 * Thrown when backend service fails. This could be due to a SQLException,
 * IOException, or any other failure that's not caused by the user.
 */
public class BackendException extends Exception {

	private static final long serialVersionUID = 4734413074392861243L;


	public BackendException( String message ) {
		super( message );
	}

}
