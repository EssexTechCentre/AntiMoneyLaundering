package com.prep;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.junit.Test;
import com.prep.AML.Account;
import com.prep.AML.Transaction;
import com.prep.AML.TransactionAccount;

public class AccountTest {

	@Test
	public void account_transactionAmountBelowThreshold_breachNotRaised() {
		
		//Arrange
		Account account = new TransactionAccount(1);		
		
		//Act
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 10), 10000, 1));
		
		//Assert
		assertFalse(account.alertRaised());
	}
	
	@Test
	public void account_transactionAmountAboveThreshold_breachRaised() {
		
		//Arrange
		Account account = new TransactionAccount(1);		
		
		//Act
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 10), 10000, 1));
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 20), 20000, 1));
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 30), 30000, 1));
		
		//Assert
		assertTrue(account.alertRaised());
	}
	
	@Test
	public void account_transactionAmountAboveThresholdOutsideOf60Seocnds_breachNotRaised() {
		
		//Arrange
		Account account = new TransactionAccount(1);		
		
		//Act
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 10), 10000, 1));
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 20), 20000, 1));
		account.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 15, 10), 30000, 1));
		
		//Assert
		assertFalse(account.alertRaised());
	}
	
	@Test
	public void account_transactionAmountBelowThresholdMultiAccounts_breachNotRaised() {
		
		//Arrange
		Account account1 = new TransactionAccount(1);
		Account account2 = new TransactionAccount(2);	
		
		//Act
		account1.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 10), 10000, 1));
		account2.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 20), 20000, 2));
		account1.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 30), 30000, 1));
		
		//Assert
		assertFalse(account1.alertRaised());
		assertFalse(account2.alertRaised());
	}
	
	@Test
	public void account_transactionAmountAboveThresholdMultiAccounts_breachRaised() {
		
		//Arrange
		Account account1 = new TransactionAccount(1);
		Account account2 = new TransactionAccount(2);	
		
		//Act
		account1.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 10), 10000, 1));
		account2.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 20), 20000, 2));
		account2.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 30), 40000, 2));
		account1.addTransaction(new Transaction(LocalDateTime.of(2024, 8, 26, 10, 10, 40), 50000, 1));
		
		//Assert
		assertTrue(account1.alertRaised());
		assertTrue(account2.alertRaised());
	}
}