package com.prep;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import com.prep.AML.CsvFileReader;
import com.prep.AML.FileReader;
import com.prep.AML.TransactionQueue;

public class CsvFileReaderTest {
	
	String filePath = "src//test//resources//";
	
	@Test
	public void processCsvFile_transactionsForAccount_addedToTransactionQueue() {
		
		//Arrange
		FileReader csvFileReader = new CsvFileReader(filePath);
		
		//Act
		try {
			csvFileReader.processCsvFile(filePath + "AML.csv");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
				
		//Assert
		Assert.assertFalse(TransactionQueue.isEmpty());
	}
	
	@Test
	public void processCsvFile_incorrectPathSupplied_exceptionThrown() {
		
		//Arrange
		FileReader csvFileReader = new CsvFileReader(filePath);
		
		//Act

		//Assert
		Assertions.assertThrows(IOException.class, () -> { csvFileReader.processCsvFile(filePath + "AML101.csv"); });
	}
	
	@Test
	public void processCsvFile_emptyFile_noTransactionsProcessed() {
		
		//Arrange
		CsvFileReader csvFileReader = new CsvFileReader(filePath);
		
		//Act
		try {
			csvFileReader.processCsvFile(filePath + "AML2.csv");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}

		//Assert
		Assert.assertTrue(TransactionQueue.isEmpty());		
	}
}