package com.banking.enums;

public enum AccountType {
	SAVINGS("SAVINGS"),
	CURRENT("CURRENT");
	
	private final String value;
	
	private AccountType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
