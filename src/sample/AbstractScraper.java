package sample;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public abstract class AbstractScraper {
	
	protected WebDriver webDriver;
	
	public AbstractScraper() {
		System.setProperty(getDriverName(), getDriverPath());
	}
	
	public void openSite(String url) {
		webDriver.get(url);
//		webDriver.navigate().to(url);
		
		try {
			webDriver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new IllegalStateException("Can't start Web Driver", e);
		}
	}

	public void closeBrowser() {
		//close the browser
	    webDriver.close();
	    webDriver.quit();
	}
	
	protected abstract int getTimeout();
	
	protected abstract String getStartURL();
	
	protected String getDriverPath() {
		return "driver/chromedriver.exe";
	}
	
	protected String getDriverName() {
		return "webdriver.chrome.driver";
	}
	
	public void start() {
		openSite(getStartURL());
	}
}
