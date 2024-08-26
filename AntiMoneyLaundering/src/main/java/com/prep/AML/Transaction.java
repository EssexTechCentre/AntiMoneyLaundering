package com.prep.AML;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Transaction {

	private final LocalDateTime time;
	private final int amount;
	private final int account;
	private final long timestamp;
	
	public Transaction(LocalDateTime time, int amount, int account) {
		this.time = time;
		this.amount = amount;
		this.account = account;
		this.timestamp = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	@Override
	public String toString() {
		return "Account: " + account + " time: " + time + " amount: " + amount;
	}

	public int getAccount() {
		return account;
	}
	
	public long getTimeStamp() {
		return timestamp;
	}
	
	public int getAmount() {
		return amount;
	}
}
