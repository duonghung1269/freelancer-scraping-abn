package main;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import model.AbnCollection;

import sample.AbnScraper;

public class MainThread {

	public static void scrapEmail() {
		Thread serverThread = new Thread() {
			public void run() {
				try {
					String parsedUrlXmlFile = "updated_url_timeout_second_part_parsed_url_2_9001_12000.xml";
					String indexRange = "9001_12000";
					String outputXmlFile = "second_part_parsed_email_2_2_9001_12000_final.xml";
					new Main().parseEmail(new String[] { parsedUrlXmlFile, indexRange, outputXmlFile });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread clientThread = new Thread() {
			public void run() {
				try {
					String parsedUrlXmlFile = "updated_url_timeout_second_part_parsed_url_2_12001_15000.xml";
					String indexRange = "12001_15000";
					String outputXmlFile = "second_part_parsed_email_2_12001_15000_final.xml";
					new Main().parseEmail(new String[] { parsedUrlXmlFile, indexRange, outputXmlFile });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		serverThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		clientThread.start();
	}
	
	public static void scrapeUrl() {
		Thread serverThread = new Thread() {
			public void run() {
				try {
					String inputFileName = "second_part_2_30001_33000.txt";
					String parsedUrlXmlFileName = "second_part_parsed_url_2_30001_33000.xml";
					new Main().parseUrl(new String[] {inputFileName, parsedUrlXmlFileName });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread clientThread = new Thread() {
			public void run() {
				try {
					String inputFileName = "second_part_2_33001_36000.txt";
					String parsedUrlXmlFileName = "second_part_parsed_url_2_33001_36000.xml";
					new Main().parseUrl(new String[] {inputFileName, parsedUrlXmlFileName });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		serverThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		clientThread.start();
				
	}
	
	public static void scrapeTimeoutUrl() {
		Thread serverThread = new Thread() {
			public void run() {
				try {
					String timeoutFileNameXml = "parsed_url/timeout/second_part_parsed_url_2_15001_18000_timeoutAbn.xml";
					String parsedUrlXmlFileName = "second_part_parsed_url_reparsed_2_15001_18000.xml";
					new Main().parseTimeOutUrl(new String[] {timeoutFileNameXml, parsedUrlXmlFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread clientThread = new Thread() {
			public void run() {
				try {
					String inputFileName = "parsed_url/timeout/second_part_parsed_url_2_50001_50436_timeoutAbn.xml";
					String parsedUrlXmlFileName = "second_part_parsed_url_reparsed_2_50001_50436.xml";
					new Main().parseTimeOutUrl(new String[] {inputFileName, parsedUrlXmlFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		serverThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		clientThread.start();
				
	}
	
	public static void syncUrlTimeoutToMainFile() {
		Thread serverThread = new Thread() {
			public void run() {
				try {
					String timeoutFileNameXml = "second_part_parsed_url_reparsed_2_42001_45000.xml";
					String parsedUrlXmlFileName = "second_part_parsed_url_2_42001_45000.xml";
					new Main().syncUrlTimeoutToMainFile(new String[] {timeoutFileNameXml, parsedUrlXmlFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread clientThread = new Thread() {
			public void run() {
				try {
					String inputFileName = "second_part_parsed_url_reparsed_2_45001_48000.xml";
					String parsedUrlXmlFileName = "second_part_parsed_url_2_45001_48000.xml";
					new Main().syncUrlTimeoutToMainFile(new String[] {inputFileName, parsedUrlXmlFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		serverThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		clientThread.start();
				
		Thread serverThread2 = new Thread() {
			public void run() {
				try {
					String timeoutFileNameXml = "second_part_parsed_url_reparsed_2_48001_50000.xml";
					String parsedUrlXmlFileName = "second_part_parsed_url_2_48001_50000.xml";
					new Main().syncUrlTimeoutToMainFile(new String[] {timeoutFileNameXml, parsedUrlXmlFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread clientThread2 = new Thread() {
			public void run() {
				try {
					String inputFileName = "second_part_parsed_url_reparsed_2_50001_50436.xml";
					String parsedUrlXmlFileName = "second_part_parsed_url_2_50001_50436.xml";
					new Main().syncUrlTimeoutToMainFile(new String[] {inputFileName, parsedUrlXmlFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		serverThread2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		clientThread2.start();
	}
	
	public static void writeToCsvFile() {
		Thread serverThread = new Thread() {
			public void run() {
				try {
					String parsedFileNameXml = "updated_url_timeout_second_part_parsed_url_2_6001_9000.xml";
					String csvFileName = "updated_url_timeout_second_part_parsed_url_2_6001_9000.csv";
					new Main().writeToCsvFile(new String[] {parsedFileNameXml, csvFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread clientThread = new Thread() {
			public void run() {
				try {
					String parsedFileNameXml = "updated_url_timeout_second_part_parsed_url_2_9001_12000.xml";
					String csvFileName = "updated_url_timeout_second_part_parsed_url_2_9001_12000.csv";
					new Main().writeToCsvFile(new String[] {parsedFileNameXml, csvFileName });
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		serverThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		clientThread.start();
				
	}
	
	private static void fillEmailToExcel() {
		AbnCollection abnCollection;
		try {
			abnCollection = AbnScraper.readAllCsvFiles();
//			abnCollection = AbnScraper.readAbnCsvFile("abn_parsed_part_2_3000.csv");
			AbnScraper.fillEmailToExcel("ACNC_The_remaining_list.xlsx", abnCollection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		scrapeUrl();
//		scrapeTimeoutUrl();
//		syncUrlTimeoutToMainFile();
//		writeToCsvFile();
//		scrapEmail();
		
		fillEmailToExcel();
	}

	
}
