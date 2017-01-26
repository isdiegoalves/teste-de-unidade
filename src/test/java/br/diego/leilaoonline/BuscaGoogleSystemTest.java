package br.diego.leilaoonline;

import static br.diego.leilaoonline.infra.selenium.WebDrivers.chromeWebDriver;
import static br.diego.leilaoonline.infra.selenium.WebDrivers.firefoxWebDriver;
import static br.diego.leilaoonline.infra.selenium.WebDrivers.ieWebDriver;
import static br.diego.leilaoonline.infra.selenium.WebDrivers.phantomJSWebDriver;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BuscaGoogleSystemTest {

	@Test
	public void googleBuscaIE() {
		WebDriver driver = ieWebDriver();
		
		driver.get("https://www.google.com.br/");

		WebElement campoDeTexto = driver.findElement(By.name("q"));
		campoDeTexto.sendKeys("InternetDriver");
		campoDeTexto.submit();

		driver.quit();
	}

	@Test
	public void googleBuscaChrome() throws InterruptedException {
		WebDriver driver = chromeWebDriver();

		driver.get("https://www.google.com.br/");

		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();

		driver.quit();
	}

	@Test
	public void googleBuscaFirefox() throws InterruptedException {
		WebDriver driver = firefoxWebDriver();

		driver.get("https://www.google.com.br/");

		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("FirefoxDriver");
		searchBox.submit();

		driver.quit();
	}
	
	@Test
	public void googleBuscaPhantomJS() throws InterruptedException {
		WebDriver driver = phantomJSWebDriver();

		driver.get("https://www.google.com.br/");

		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();

		driver.quit();
	}
}
