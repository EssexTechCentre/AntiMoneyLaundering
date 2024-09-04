package com.prep.AML;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App {
		
	static String filePath = "src//main//resources//";
	private static final int NO_OF_PROCESS_SLAVES = 5;

	//NOTES:
	//(1)  Please see src/main/doc "aml design.PNG" for task design 
	//(2)  To run executable (args[0] is location of your AML.CVS file) - 
	//			java -jar AntiMoneyLaundering-0.0.1.jar C:\User\Temp
	//(3)  Assumed Java 8 for compatibility.
	//(4)  Core java only used, no external third party loggers, data structures etc 
	//(5)  Running as an executable one can pass in a directory to read AML files from
	
	public static void main(String [] args) {
		
		if(args.length == 1) {
			System.out.println("Override filePath with input parameter: " + args[0]);
			filePath = args[0];
		}
		
		new Thread(new CsvFileReader(filePath)).start();
		
		Executor executor = Executors.newFixedThreadPool(5); 
		for(int processSlave = 0; processSlave < NO_OF_PROCESS_SLAVES; processSlave++) {
			executor.execute(new Thread(new TransactionProcessorSlave(processSlave)));
		}
	}
}















