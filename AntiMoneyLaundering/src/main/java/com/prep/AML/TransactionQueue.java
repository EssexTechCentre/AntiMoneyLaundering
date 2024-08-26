package com.prep.AML;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//NOTES:
// (1) - accounts assigned to different queues for processing.  If certain accounts are higher risk with more chance 
//       of AML this system can be adapted for certain queues with higher priority processing or less accounts to a queue for less latency 

public class TransactionQueue implements Queue {

	private static List<BlockingQueue<Transaction>> transactionBlockingQueue = new ArrayList<BlockingQueue<Transaction>>();
	private static TransactionQueue transactionQueue;
	private TransactionQueue() {}
	
	protected static synchronized TransactionQueue getInstance() {
		if(transactionQueue == null) {
			System.out.println("Creating instance of TransactionQueue");
			transactionQueue = new TransactionQueue();
			transactionBlockingQueue.add(new LinkedBlockingQueue<Transaction>());
			transactionBlockingQueue.add(new LinkedBlockingQueue<Transaction>());
			transactionBlockingQueue.add(new LinkedBlockingQueue<Transaction>());
			transactionBlockingQueue.add(new LinkedBlockingQueue<Transaction>());
			transactionBlockingQueue.add(new LinkedBlockingQueue<Transaction>());
		}
		return transactionQueue;
	}

	public void put(Transaction transaction) {		
		try {
			if(transaction.getAccount() > 0 && transaction.getAccount() <= 1000) {
				transactionBlockingQueue.get(0).put(transaction);
			} else if (transaction.getAccount() > 1000 && transaction.getAccount() <= 2000) {
				transactionBlockingQueue.get(1).put(transaction);				
			} else if (transaction.getAccount() > 2000 && transaction.getAccount() <= 3000) {
				transactionBlockingQueue.get(2).put(transaction);				
			} else if (transaction.getAccount() > 3000 && transaction.getAccount() <= 4000) {
				transactionBlockingQueue.get(3).put(transaction);				
			} else if (transaction.getAccount() > 4000 && transaction.getAccount() <= 5000) {
				transactionBlockingQueue.get(4).put(transaction);				
			} else {
				System.out.println("Unrecognised account id: " + transaction.getAccount());
				return;
			}
			System.out.println("Transaction placed on the TransactionQueue: " + transaction);
		} catch (InterruptedException ie) {
			System.out.println("Exception placing the transaction on the TransactionQueue: " + ie.getMessage());
		}
	}
	
	public Transaction take(int id) {
		Transaction transaction = null;
		try {
			transaction = transactionBlockingQueue.get(id).take();
			System.out.println("Transaction taken off of the TransactionQueue" + transaction);
		} catch (InterruptedException ie) {
			System.out.println("Exception taking the transaction off of the TransactionQueue: " + ie.getMessage());
		}
		return transaction;
	}
	
	public static boolean isEmpty() {
		return transactionBlockingQueue.get(0).isEmpty() 
				&& transactionBlockingQueue.get(1).isEmpty()
				&& transactionBlockingQueue.get(2).isEmpty()
				&& transactionBlockingQueue.get(3).isEmpty()
				&& transactionBlockingQueue.get(4).isEmpty();
	}
}
