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
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
		WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("D:\\Aplic32\\Mozilla Firefox\\firefox.exe")), profile);
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