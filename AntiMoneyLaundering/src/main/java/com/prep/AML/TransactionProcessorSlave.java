package com.prep.AML;

public class TransactionProcessorSlave extends Thread {

	private TransactionQueue transactionQueue;
	private AccountContainer accountContainer;
	private int id;
	
	public TransactionProcessorSlave(int id){
		this.id = id;
		this.transactionQueue = TransactionQueue.getInstance();
		this.accountContainer = AccountContainer.getInstance();
	}
	
	@Override
	public void run() {		
		
		while(true) {
			Transaction transaction = transactionQueue.take(id);
			System.out.println("Slave processing is id: " + id);
			
			Account account = accountContainer.getAccount(transaction.getAccount());			
			account.addTransaction(transaction);
		}
	}
}
