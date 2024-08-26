package com.prep.AML;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static java.nio.file.StandardWatchEventKinds.*;

//NOTES
//(1) this class has a file watcher to catch newly dropped AML files and process them

public class CsvFileReader extends Thread implements FileReader {

	private TransactionQueue transactionQueue;
	private final String filePath;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
	
	public CsvFileReader(String filePath) {
		this.transactionQueue = TransactionQueue.getInstance();
		this.filePath = filePath;
	}
	
	public void processCsvFile(String filePath) throws IOException {

		try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath))) {
			
			String line;
			while((line = bufferedReader.readLine()) != null) {
				System.out.println("Processing line from csv file: " + line);

				List<String> transaction = Arrays.asList(line.split(","));				
				transactionQueue.put(new Transaction(LocalDateTime.parse(transaction.get(0), dateTimeFormatter), 
														Integer.valueOf(transaction.get(1)), 
														Integer.valueOf(transaction.get(2))));
			}
		} catch (IOException io) {
			System.out.println("Exception thrown in processCsvFile: " + io.getMessage());
			throw io;
		}
	}
	
	@Override
	public void run() {

		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();		
			Path csvFilePath = Paths.get(filePath);
			csvFilePath.register(watchService, ENTRY_CREATE);
			
			if(new File(csvFilePath + "//AML.csv").exists()) {
				processCsvFile(csvFilePath + "//AML.csv");
			} else {

	            WatchKey watchKey = watchService.take();
	            for (WatchEvent<?> event : watchKey.pollEvents()) {
	            	
	                WatchEvent.Kind<?> kind = event.kind();
	
	                if (ENTRY_CREATE.equals(kind)) {
	                	System.out.println("New csv File received");
	                	processCsvFile(filePath + "//AML.csv");                    
	                }
	            }
	            watchKey.reset();
			}
		} catch (IOException | InterruptedException exception) {
			System.out.println("Exception thrown processing the CsvFile: " + exception.getMessage());
		}
	}
}
