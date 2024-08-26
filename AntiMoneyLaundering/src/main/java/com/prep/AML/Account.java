package com.prep.AML;

public interface Account {

	public boolean alertRaised();
	
	public void clearAlert();
	
	public void addTransaction(Transaction transaction);
}
