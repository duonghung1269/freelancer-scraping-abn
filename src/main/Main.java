package main;

import java.io.IOException;
import java.util.List;

import model.Abn;
import model.AbnCollection;

import sample.AbnScraper;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		AbnScraper scraper = new AbnScraper();
		long start = System.currentTimeMillis();
		System.out.println("====star: t" + start);
		scraper.start();
		List<Abn> abns = scraper.loadAbnFromFile("src/input/first_part.txt");
		AbnCollection abnCollection = scraper.scrap(abns);
		AbnScraper.marshalToXML(abnCollection, "src/input/first_part_parse.xml");
		long end = System.currentTimeMillis();
		System.out.println("=--- spend: " + (end - start));
		scraper.closeBrowser();
	}

}
