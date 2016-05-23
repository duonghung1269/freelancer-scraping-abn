package main;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public class MainThread {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// second_part_2_2_3000.txt second_part_parsed_email_2_2_3000.xml

		Thread serverThread = new Thread() {
			public void run() {
				try {
					Main.main(new String[] { "second_part_2_2_3000.txt",
							"second_part_parsed_email_2_2_3000.xml" });
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
					Main.main(new String[] { "second_part_2_3001_6000.txt",
							"second_part_parsed_email_2_2_3001_6000.xml" });
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

}
