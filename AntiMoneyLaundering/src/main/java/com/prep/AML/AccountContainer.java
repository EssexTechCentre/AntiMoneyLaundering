package com.prep.AML;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountContainer {

	private Map<Integer, Account> accounts = new ConcurrentHashMap<>();
	private static AccountContainer accountContainer;
	private static final int NO_OF_ACCOUNTS = 5000;
	
	private AccountContainer() {
		for(int account = 1; account <= NO_OF_ACCOUNTS; account++) {
			accounts.put(account, new TransactionAccount(account));
		}
	}
	
	public static synchronized AccountContainer getInstance() {
		
		if(accountContainer == null) {
			System.out.println("Creating instance of AccountContainer");
			accountContainer = new AccountContainer();
		}
		return accountContainer;
	}
	
	protected Account getAccount(int accountId) {
		return accounts.get(accountId);
	}
}
