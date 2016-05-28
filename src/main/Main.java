package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import model.Abn;
import model.AbnCollection;
import sample.AbnScraper;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	public void parseUrl(String[] args) throws IOException, JAXBException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Please inpute the abn txt file !");
		}
		AbnScraper scraper = new AbnScraper();
		long start = System.currentTimeMillis();
		System.out.println("====star: t" + start);
		scraper.start();
		List<Abn> abns = scraper.loadAbnFromFile(args[0]);
		String parsedFileName = args[1].substring(0, args[1].indexOf("."));
		scraper.scrap(abns, parsedFileName);
//		AbnScraper.marshalToXML(abnCollection, args[1]);
//		AbnScraper.marshalToXML(abnCollection, "first_part_parse.xml");
//		AbnCollection unmarshalToAbnCollection = AbnScraper.unmarshalToAbnCollection("first_part_parsed_url_final.xml");
//		List<Abn> newList = unmarshalToAbnCollection.getAbns().subList(1801, unmarshalToAbnCollection.getAbns().size());
//		AbnCollection newCollection = new AbnCollection();
//		newCollection.getAbns().addAll(newList);
//		scraper.scrapeEmail(newCollection.getAbns());
		long end = System.currentTimeMillis();
//		AbnScraper.marshalToXML(unmarshalToAbnCollection, "first_part_parsed_email_final.xml");
		System.out.println("=--- spend: " + (end - start));
//		scraper.closeBrowser();
	}

	public void parseEmail(String[] args) throws IOException, JAXBException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Please inpute the abn txt file !");
		}
		AbnScraper scraper = new AbnScraper();
		long start = System.currentTimeMillis();
		System.out.println("====star: t" + start);
		AbnCollection unmarshalToAbnCollection = AbnScraper.unmarshalToAbnCollection(args[0]);
//		List<Abn> newList = unmarshalToAbnCollection.getAbns().subList(1801, unmarshalToAbnCollection.getAbns().size());
		AbnCollection newCollection = new AbnCollection();
		newCollection.getAbns().addAll(unmarshalToAbnCollection.getAbns());
		scraper.scrapeEmail(newCollection.getAbns(), args[1]);
		long end = System.currentTimeMillis();
		AbnScraper.marshalToXML(unmarshalToAbnCollection, args[2]);
		System.out.println("=--- spend: " + (end - start));
//		scraper.closeBrowser();
	}
	
	public void parseTimeOutUrl(String[] args) throws JAXBException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Please inpute the abn time out file !");
		}
		AbnScraper scraper = new AbnScraper();
		scraper.start();
		long start = System.currentTimeMillis();
		System.out.println("====star: t" + start);
		AbnCollection unmarshalToAbnCollection = AbnScraper.unmarshalToAbnCollection(args[0]);
		String parsedFileName = args[1].substring(0, args[1].indexOf("."));
		scraper.scrap(unmarshalToAbnCollection.getAbns(), parsedFileName);
		long end = System.currentTimeMillis();
		System.out.println("=--- spend: " + (end - start));
	}
	
	public void syncUrlTimeoutToMainFile(String[] args) throws JAXBException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Please inpute the abn from file and to file !");
		}
		
//		AbnCollection fromTimeoutXmlFile = AbnScraper.unmarshalToAbnCollection(args[0]);
//		AbnCollection toXmlFile = AbnScraper.unmarshalToAbnCollection(args[1]);
		
		AbnScraper.updateUrlTimeout(args[0], args[1]);
	}
	
	public void writeToCsvFile(String[] args) throws JAXBException, IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Please inpute the abn from file and to file !");
		}
		
//		AbnCollection fromTimeoutXmlFile = AbnScraper.unmarshalToAbnCollection(args[0]);
//		AbnCollection toXmlFile = AbnScraper.unmarshalToAbnCollection(args[1]);
		
		AbnScraper.writeToCsvFile(args[0], args[1]);
	}
}
