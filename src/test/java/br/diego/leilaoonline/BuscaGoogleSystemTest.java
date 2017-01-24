package br.diego.leilaoonline;

import static java.lang.System.setProperty;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BuscaGoogleSystemTest {

	@Test
	public void googleBuscaIE() {
		setProperty("webdriver.ie.driver","src/test/resources/selenium/iedriver.exe");
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https://www.google.com.br/");

		WebElement campoDeTexto = driver.findElement(By.name("q"));
		campoDeTexto.sendKeys("InternetDriver");
		campoDeTexto.submit();

		driver.quit();
	}

	@Test
	public void googleBuscaChrome() throws InterruptedException {
		setProperty("webdriver.chrome.driver", "src/test/resources/selenium/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https://www.google.com.br/");

		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();

		driver.quit();
	}

	@Test
	public void googleBuscaFirefox() throws InterruptedException {
		setProperty("webdriver.gecko.driver", "src/test/resources/selenium/geckodriver.exe");
		FirefoxProfile profile = new FirefoxProfile();
		WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("D:\\Aplic32\\Mozilla Firefox\\firefox.exe")), profile);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https://www.google.com.br/");

		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("FirefoxDriver");
		searchBox.submit();

		driver.quit();
	}
	
	@Test
	public void googleBuscaPhantomJS() throws InterruptedException {
		setProperty("phantomjs.binary.path", "src/test/resources/selenium/phantomjs.exe");
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setJavascriptEnabled(true);
		WebDriver driver = new PhantomJSDriver(dc);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https://www.google.com.br/");

		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();

		driver.quit();
	}
}
