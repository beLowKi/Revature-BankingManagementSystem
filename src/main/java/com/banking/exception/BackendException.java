package com.banking.exception;

/**
 * Thrown when backend service fails. This could be due to a
 * SQLException, IOException, or any other failure that's not
 * caused by the user.
 */
public class BackendException extends Exception {
	public BackendException(String message) {
		super(message);
	}
}
