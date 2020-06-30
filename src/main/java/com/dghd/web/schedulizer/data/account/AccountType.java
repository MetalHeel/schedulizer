package com.dghd.web.schedulizer.data.account;

public enum AccountType {
	ADMIN("Admin"),
	BAND("Band"),
	BOOKER("Booker");
	
	private final String displayValue;
	
	private AccountType(String displayValue) {
		this.displayValue = displayValue;
	}
	
	public static AccountType[] getRegisterableValues() {
		return new AccountType[]{ AccountType.BAND, AccountType.BOOKER };
	}
	
	public String getDisplayValue() {
		return displayValue;
	}
};
