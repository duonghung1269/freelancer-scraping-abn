package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Abn;
import model.AbnCollection;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbnScraper extends AbstractScraper {

	private static final int DEFAULT_FIND_ABN_TIMEOUT = 20; // in seconds

	public AbnScraper() {
		super();
		this.webDriver = new ChromeDriver();
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

		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String line = null;
		while ((line = br.readLine()) != null) {
			list.add(new Abn(line.trim()));
		}

		br.close();

		return list;
	}

	public AbnCollection scrap(List<Abn> list) {
		AbnCollection abnCollection = new AbnCollection();
		for (int i = 0; i < list.size(); i++) {
			WebElement abnTextfield = webDriver
					.findElement(By
							.id("ctl00_TemplateBody_WebPartManager1_gwpciNewQueryMenuCommon_ciNewQueryMenuCommon_ResultsGrid_Sheet0_Input1_TextBox1"));
			Abn abn = list.get(i);
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
		}
		
		return abnCollection;
	}

	public static void marshalToXML(AbnCollection abnCollection, String fileNameXML) {
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(AbnCollection.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			File f = new File(fileNameXML);
			jaxbMarshaller.marshal(abnCollection, f);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
