package com.banking.exception;

public class InsufficientBalanceException extends Exception {

	private static final long serialVersionUID = 6700415203367773346L;

	public InsufficientBalanceException( String message ) {
		super( message );
	}

}
