package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.Abn;
import model.AbnCollection;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.beust.jcommander.Strings;

public class AbnScraper extends AbstractScraper {

	private static final int DEFAULT_FIND_ABN_TIMEOUT = 20; // in seconds
	private static final int MAXIMUM_RECORDS_PARSED_PER_FILE = 1000;
	private static final String INPUT_PATH = "src/input/";
	private static final String OUTPUT_PATH = "src/output/";
	private static final String PYTHON_PATH = "src/python/";
	private static final String TIMEOUT_URL = "TIME_OUT";
	
	private static final String FINAL_PARSED_FILE_NAME = "abn_parsed_part_";
	private static final String FINAL_PARSED_FILE_EXTENSION = ".csv";

	public AbnScraper() {
		super();
//		ChromeOptions options = new ChromeOptions();
//		HashMap<String, Object> prefs = new HashMap<>();
//		prefs.put("profile.managed_default_content_settings.images", 2);
//		
//		DesiredCapabilities chromeCaps = DesiredCapabilities.chrome();
//		chromeCaps.setJavascriptEnabled(false);
//		chromeCaps.setCapability(ChromeOptions.CAPABILITY, options);
		
		ChromeOptions op = new ChromeOptions();
	    op.addExtensions(new File("C:\\chrome_block_image\\Block-image_v1.0.crx"));
		this.webDriver = new ChromeDriver(op);
		
//		this.webDriver = new PhantomJSDriver();
	}

	@Override
	protected int getTimeout() {
		return 10;
	}

	@Override
	protected String getStartURL() {
		return "http://www.acnc.gov.au/ACNC/FindCharity/QuickSearch/ACNC/OnlineProcessors/Online_register/Search_the_Register.aspx?noleft=1";
	}

	public List<Abn> loadAbnFromFile(String filename) throws IOException {
		List<Abn> list = new ArrayList<>();

		File f = new File(INPUT_PATH + filename);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String line = null;
		while ((line = br.readLine()) != null) {
			list.add(new Abn(line.trim()));
		}

		br.close();

		return list;
	}

	public AbnCollection scrap(List<Abn> list, String parsedFileName) {
		AbnCollection abnCollection = new AbnCollection();
		AbnCollection timeOutAbnCollection = new AbnCollection();
		int fileIndex = 1;
		int abnsSize = list.size();
		for (int i = 0; i < abnsSize; i++) {
			Abn abn = list.get(i);
			try {
				
				WebElement abnTextfield = webDriver
						.findElement(By
								.id("ctl00_TemplateBody_WebPartManager1_gwpciNewQueryMenuCommon_ciNewQueryMenuCommon_ResultsGrid_Sheet0_Input1_TextBox1"));
				abnTextfield.clear();
				abnTextfield.sendKeys(abn.getAbn());
				abnTextfield.sendKeys(Keys.ENTER);
				
				WebDriverWait wait = new WebDriverWait(webDriver,
						DEFAULT_FIND_ABN_TIMEOUT);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath("//table[@class='rgMasterTable']")));
				String url = webDriver.findElements(
						By.xpath("//table[@class='rgMasterTable']/tbody/tr//a"))
						.isEmpty() ? ""
								: webDriver
								.findElements(
										By.xpath("//table[@class='rgMasterTable']/tbody/tr//a"))
										.get(0).getAttribute("href");
				abn.setUrl(url);
				abnCollection.getAbns().add(abn);
				System.out.println(i);
				
				if ((i > 0 && i % MAXIMUM_RECORDS_PARSED_PER_FILE == (MAXIMUM_RECORDS_PARSED_PER_FILE - 1)) || (i == abnsSize - 1)) {
					marshalToXML(abnCollection, parsedFileName + fileIndex + ".xml");
					abnCollection.getAbns().clear();
					System.out.println("Parsed file index " + fileIndex);
					
					marshalToXML(timeOutAbnCollection, parsedFileName + "_" + fileIndex + "_timeoutAbn.xml");
					timeOutAbnCollection.getAbns().clear();
					
					fileIndex++;
					
//					// sleep for 10 seconds
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}
				
				if (i > 0 && i % 300 == 0) {
					// sleep for 10 seconds
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				abn.setUrl(TIMEOUT_URL);
				abnCollection.getAbns().add(abn);
				timeOutAbnCollection.getAbns().add(abn);
				
//				String title = webDriver.findElement(By.tagName("title")).getText().toLowerCase().trim();
//				System.out.println("=====title: " + title);
//				if (title.contains("sign in")) { // navigate to main page
					start(); 
//				}
//				WebElement sign_in = webDriver
//						.findElement(By
//								.id("ctl00_TemplateBody_WebPartManager1_gwpciNewContactSignInCommon_ciNewContactSignInCommon_signInUserName"));
				
			}
		}
		
		
		
		return abnCollection;
	}

	public void scrapeEmail(List<Abn> abns, String emailPrefixFileName) throws IOException {
//		Document doc;
		
		AbnCollection abnTimeoutEmailsCollection = new AbnCollection();
		
		StringBuilder sb = new StringBuilder();
		final String NEW_LINE = System.getProperty("line.separator");
		
		int abnsSize = abns.size();
		int fileIndex = 1;
		for (int i = 0; i < abnsSize; i++) {
			Abn abn = abns.get(i);
			
			if (TIMEOUT_URL.equals(abn.getUrl())) {
				abnTimeoutEmailsCollection.getAbns().add(abn);
				sb.append(abn.toString()).append(NEW_LINE);
				continue;
			}
			try {
//				openSite(abn.getUrl());
				
				webDriver.get(abn.getUrl());
				WebElement emailElement = (new WebDriverWait(webDriver, getTimeout())).until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email")));
				
//				WebElement emailElement = webDriver.findElement(By.id("ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email"));
				// need http protocol
//				doc = Jsoup.connect(abn.getUrl())
//						   .timeout(DEFAULT_FIND_ABN_TIMEOUT * 1000)
//						   .userAgent("Mozilla")
////						   .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
//						   .get();
				
				// get page title
//				String title = doc.title();
//				System.out.println("title : " + title);
				
				// get all links
//				Element emailElement = doc.getElementById("ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email");
				abn.setEmail(emailElement.getText().trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sb.append(abn.toString()).append(NEW_LINE);
			
			if ((i > 0 && i % MAXIMUM_RECORDS_PARSED_PER_FILE == (MAXIMUM_RECORDS_PARSED_PER_FILE - 1)) || (i == abns.size() - 1) ) {
				File newFile = new File(OUTPUT_PATH + FINAL_PARSED_FILE_NAME + emailPrefixFileName + "_" + fileIndex + FINAL_PARSED_FILE_EXTENSION);
				BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
				writer.append(sb.toString());
				writer.close();
				
				fileIndex++;
				
				// clear string builder
				sb = new StringBuilder();
				
				// sleep for 10 seconds
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(i + 1);
		}
		
	}
	
	public void scrapeEmailJSoup(List<Abn> abns) throws IOException {
		Document doc;
		
		AbnCollection abnTimeoutEmailsCollection = new AbnCollection();
		
		StringBuilder sb = new StringBuilder();
		final String NEW_LINE = System.getProperty("line.separator");
		
		int abnsSize = abns.size();
		
		for (int i = 0; i < abnsSize; i++) {
			Abn abn = abns.get(i);
			
			if (TIMEOUT_URL.equals(abn.getUrl())) {
				abnTimeoutEmailsCollection.getAbns().add(abn);
				sb.append(abn.toString()).append(NEW_LINE);
				continue;
			}
			try {
//				openSite(abn.getUrl());
//				WebElement emailElement = webDriver.findElement(By.id("ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email"));
				// need http protocol
				doc = Jsoup.connect(abn.getUrl())
						   .timeout(DEFAULT_FIND_ABN_TIMEOUT * 1000)
						   .userAgent("Mozilla")
//						   .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
						   .get();
				
				// get page title
				String title = doc.title();
				System.out.println("title : " + title);
				
				// get all links
				Element emailElement = doc.getElementById("ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email");
				abn.setEmail(emailElement.text().trim());
				System.out.println(abn.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sb.append(abn.toString()).append(NEW_LINE);
			
			if ((i > 0 && i % MAXIMUM_RECORDS_PARSED_PER_FILE == 0) || (i == abns.size() - 1) ) {
				File newFile = new File(OUTPUT_PATH + FINAL_PARSED_FILE_NAME + FINAL_PARSED_FILE_EXTENSION);
				BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
				writer.append(sb.toString());
				writer.close();
				
				// clear string builder
				sb = new StringBuilder();
				
				// sleep for 10 seconds
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(i + 1);
		}
		
	}
	
	public static void writeToCsvFile(List<Abn> abns) {
		
	}
	
	public static void main(String[] asdas) throws IOException {
		File newFile = new File(OUTPUT_PATH + FINAL_PARSED_FILE_NAME + FINAL_PARSED_FILE_EXTENSION);
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				newFile, true));
		String newLine = System.getProperty("line.separator");
		System.out.println("New Line: " + newLine);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i ++) {
			sb.append("huhu,").append(i).append(newLine);
//			writer.write("huhu," + i);
//			writer.newLine();
		}
		writer.write(sb.toString());
		writer.close();
	}
	
	public static void marshalToXML(AbnCollection abnCollection, String fileNameXML) {
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(AbnCollection.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			File f = new File(INPUT_PATH + fileNameXML);
			jaxbMarshaller.marshal(abnCollection, f);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static AbnCollection unmarshalToAbnCollection(String fileNameXML) throws JAXBException {
        try {

        	File f = new File(INPUT_PATH + fileNameXML);
            JAXBContext jaxbContext = JAXBContext.newInstance(AbnCollection.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbnCollection abnCollection = (AbnCollection) jaxbUnmarshaller.unmarshal(f);
//            System.out.println(personCollection);
            return abnCollection;
        } catch (JAXBException e) {
            throw e;
        }
        
    }
	
	public static void updateUrlTimeout(String fromFileXml, String toFileXml) throws JAXBException {
		AbnCollection fromCollection = unmarshalToAbnCollection(fromFileXml);
		AbnCollection toCollection = unmarshalToAbnCollection(toFileXml);
		List<Abn> fromAbns = fromCollection.getAbns();
		List<Abn> toAbns = toCollection.getAbns();
		for (Abn fromAbn : fromAbns) {
			for (Abn toAbn : toAbns) {
				if (fromAbn.getAbn().equals(toAbn.getAbn())) {
					toAbn.setUrl(fromAbn.getUrl());
					continue;
				}
			}
		}
		
		marshalToXML(toCollection, "updated_url_timeout_" + toFileXml);
	}
	
	public static void writeToCsvFile(String parsedUrlXmlFile, String outputCsvFileName) throws JAXBException, IOException {
		AbnCollection fromCollection = unmarshalToAbnCollection(parsedUrlXmlFile);
//		File f = new File(PYTHON_PATH + outputCsvFileName);
		FileWriter writer = new FileWriter(PYTHON_PATH + outputCsvFileName);
		for (Abn abn : fromCollection.getAbns()) {
			writer.append(abn.getAbn());
			writer.append(',');
			writer.append(abn.getUrl());
			writer.append(',');
			writer.append('\n');
			
		}
		
		writer.flush();
	    writer.close();
	}
	
	// Utility which converts CSV to ArrayList using Split Operation
	public static AbnCollection readAbnCsvFile(String crunchifyCSV) throws IOException {
		AbnCollection abnCollection = new AbnCollection();
		List<Abn> crunchifyResult = new ArrayList<>();
		
		BufferedReader crunchifyBuffer = null;
		
		try {
			String crunchifyLine;
			crunchifyBuffer = new BufferedReader(new FileReader(OUTPUT_PATH + crunchifyCSV));
			
			// How to read file in java line by line?
			while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {
				System.out.println("Raw CSV data: " + crunchifyLine);
				String[] split = crunchifyLine.split(",");
				Abn abn = new Abn();
				abn.setAbn(split[0].trim());
				if (split.length > 1) {
					abn.setEmail(split[1].trim());
				} else {
					abn.setEmail("");
				}
				
				crunchifyResult.add(abn);
			}
			
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (crunchifyBuffer != null) crunchifyBuffer.close();
			} catch (IOException crunchifyException) {
				crunchifyException.printStackTrace();
			}
		}
		
		abnCollection.setAbns(crunchifyResult);
		
		return abnCollection;
	}
	
	// Utility which converts CSV to ArrayList using Split Operation
	public static ArrayList<String> crunchifyCSVtoArrayList(String crunchifyCSV) {
		ArrayList<String> crunchifyResult = new ArrayList<String>();
		
		if (crunchifyCSV != null) {
			String[] splitData = crunchifyCSV.split("\\s*,\\s*");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					crunchifyResult.add(splitData[i].trim());
				}
			}
		}
		
		return crunchifyResult;
	}
	
	public static AbnCollection readAllCsvFiles() throws IOException {
		File folder = new File(OUTPUT_PATH);
		File[] listOfFiles = folder.listFiles();

		AbnCollection abnCollection = new AbnCollection();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  File file = listOfFiles[i];
		  if (file.isFile() && file.getName().endsWith(".csv")) {
			  AbnCollection tmp = readAbnCsvFile(file.getName());
			  abnCollection.getAbns().addAll(tmp.getAbns());
		  } 
		}
		
		return abnCollection;
	}
	
	public static void fillEmailToExcel(String excelFileName, AbnCollection abnWithEmailCollection) throws IOException, InvalidFormatException {
		//Read the spreadsheet that needs to be updated
//		FileInputStream fsIP= new FileInputStream(new File(OUTPUT_PATH + excelFileName));
		File f = new File(OUTPUT_PATH + excelFileName);
		//Access the workbook                  
		Workbook wb = new XSSFWorkbook(f);
		//Access the worksheet, so that we can update / modify it. 
		Sheet worksheet = wb.getSheetAt(0); 
		
		Map<String, String> map = new HashMap<>();
		Abn tmpAbn = null;
		for (int i = 0; i < abnWithEmailCollection.getAbns().size(); i++) {
			tmpAbn = abnWithEmailCollection.getAbns().get(i);
			map.put(tmpAbn.getAbn(), tmpAbn.getEmail());
		}
		
		int rowNo = 1;
		int abnColNo = 0;
		int emailColNo = 1;
		String cellAbnVal = "";
		String emailVal = "";
		
		// declare a Cell object
		Cell abnCell = null;
		Cell emailCell = null;
		
		do {
			// Access the second cell in second row to update the value
			Row row = worksheet.getRow(rowNo);
			if (row == null) {
				break;
			}
			abnCell = row.getCell(abnColNo);   
			abnCell.setCellType(Cell.CELL_TYPE_STRING);
			
			emailCell = row.getCell(emailColNo, Row.CREATE_NULL_AS_BLANK);
			
			cellAbnVal = abnCell.getStringCellValue();
			try {
				emailVal = map.get(cellAbnVal);
				
				emailCell.setCellValue(emailVal);
				
			} catch (NullPointerException ex) {
				System.out.println(ex);
			}
			
			System.out.println("row: " + rowNo);
			rowNo++;			
		} while (!isCellEmpty(abnCell));
		
//		// Get current cell value value and overwrite the value
//		cell.setCellValue("OverRide existing value");
		//Close the InputStream  
//		f.close(); 
		//Open FileOutputStream to write updates
		FileOutputStream output_file =new FileOutputStream(new File(OUTPUT_PATH + "filled_" + excelFileName));  
		 //write changes
		wb.write(output_file);
		//close the stream
		output_file.close();
	}
	
	public static boolean isCellEmpty(final Cell cell) {
	    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
	        return true;
	    }

	    return false;
	}
	
//	@Override
//	protected String getDriverName() {
//		return "phantomjs.binary.path";
//	}
//	
//	@Override
//	protected String getDriverPath() {
//		return "driver/phantomjs.exe";
//	}
}
