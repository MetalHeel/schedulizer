package com.dghd.web.schedulizer.data.account;

public enum AccountType {
	BAND("Band"),
	BOOKER("Booker");
	
	private final String displayValue;
	
	private AccountType(String displayValue) {
		this.displayValue = displayValue;
	}
	
	public String getDisplayValue() {
		return displayValue;
	}
};
