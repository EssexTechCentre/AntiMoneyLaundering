package com.prep.AML;

public interface Queue {

	public void put(Transaction transaction);
	
	public Transaction take(int id);

}
