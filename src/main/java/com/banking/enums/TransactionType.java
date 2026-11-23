package com.banking.enums;

/**
 * Types of {@link Transaction}
 */
public enum TransactionType {
	DEPOSIT("DEPOSIT"), 
	WITHDRAWAL("WITHDRAWAL"), 
	TRANSFER("TRANSFER");
	
	private final String value;
	
	TransactionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
