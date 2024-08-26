package com.prep.AML;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

//NOTES
//(1) - please see src/main/doc "aml design.PNG" for task design
//(2) - assumption separate process to persist transactions to database and purge from ArrayList
//(3) - as soon as a breach occurs there is no continuation to identify if further breaches in the 60 second time window.  An alert is immediately raised with breached amount
//(4) - alerts in this class just raised to System.out, but in production to an alert monitor/processor.  Breaching transactions will be persisted and processed outside of this task
//(5) - designed such that you can have multiple transactions for the same account for the same second

public class TransactionAccount implements Account {

	private int accountId;
	private List<Transaction> transactions = new ArrayList<Transaction>();
	private static final int TIME_WINDOW = 60000;
	private static final int BREACH_AMOUNT = 50000;
	private AccountAntiMoneyLaunderingChecking accountAntiMoneyLaunderingChecking;
	private boolean alertRaised;
	
	public TransactionAccount(int accountId) {
		this.accountId = accountId;		
		accountAntiMoneyLaunderingChecking = new AccountAntiMoneyLaunderingChecking();
	}
	
	public void addTransaction(Transaction transaction) {
		synchronized(transactions) {
			transactions.add(transaction);
			accountAntiMoneyLaunderingChecking.checkTransaction();
		}
	}
	
	private void flagAlert(Transaction transaction, int breachFound) {
		alertRaised = true;
		System.out.printf("There has been an AML breach alert on account %d, for amount %d, with transaction %s", accountId, breachFound, transaction);
	}
	
	private class AccountAntiMoneyLaunderingChecking {

		private void checkTransaction() {
							
			ListIterator<Transaction> listIterator = transactions.listIterator(transactions.size());
			
			long latestTransactionTimestamp = 0;
			int amountAccumulated = 0;
			while(listIterator.hasPrevious()) {
				Transaction transaction = listIterator.previous();
				if(latestTransactionTimestamp == 0) {
					latestTransactionTimestamp = transaction.getTimeStamp();
					amountAccumulated += transaction.getAmount();
				} else {
					if((latestTransactionTimestamp - transaction.getTimeStamp()) < TIME_WINDOW) {
						amountAccumulated += transaction.getAmount();
						
						if(amountAccumulated > BREACH_AMOUNT) {
							System.out.println("AML breach found!");
							flagAlert(transactions.get(transactions.size()-1), amountAccumulated);
							break;
						}
					} else {
						System.out.println("No AML breach for latest transaction");
						break;
					}
				}
			}
			System.out.println("AML check complete");	
		}	
	}
	
	public boolean alertRaised() {
		return alertRaised;
	}
		
	public void clearAlert() {
		//a separate process will clear the alert following investigation by the Bank Branch Manager
		alertRaised = false;
	}
}







