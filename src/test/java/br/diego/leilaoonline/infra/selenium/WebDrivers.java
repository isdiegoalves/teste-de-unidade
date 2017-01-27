package br.diego.leilaoonline.infra.selenium;

import static java.lang.System.setProperty;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDrivers {

	public static WebDriver ieWebDriver() {
		setProperty("webdriver.ie.driver","src/test/resources/selenium/iedriver.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();		
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true); 
		WebDriver driver = new InternetExplorerDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver chromeWebDriver() {
		setProperty("webdriver.chrome.driver", "src/test/resources/selenium/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver firefoxWebDriver() {
		setProperty("webdriver.gecko.driver", "src/test/resources/selenium/geckodriver.exe");
		FirefoxProfile profile = new FirefoxProfile();
		String firefoxPath = System.getProperty("firefox.path", "C:\\Program Files (x86)\\Firefox Developer Edition\\firefox.exe");
		WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File(firefoxPath)), profile);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver phantomJSWebDriver() {
		setProperty("phantomjs.binary.path", "src/test/resources/selenium/phantomjs.exe");
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setJavascriptEnabled(true);
		WebDriver driver = new PhantomJSDriver(dc);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver edgeWebDriver() {
		setProperty("webdriver.edge.driver", "src/test/resources/selenium/edgedriver.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.edge();
		WebDriver driver = new EdgeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}
}