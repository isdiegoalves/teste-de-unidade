package br.diego.leilaoonline.lance.selenium.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LancesPage {

	private final WebDriver driver;

	public LancesPage(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public void lance(String usuario, double valor) {
		new Select(driver.findElement(By.name("lance.usuario.id"))).selectByVisibleText(usuario);
		driver.findElement(By.name("lance.valor")).sendKeys(String.valueOf(valor));
		driver.findElement(By.id("btnDarLance")).click();
	}
	
	public boolean existeLance(String usuario, double valor) {
		Boolean temUsuario = new WebDriverWait(driver, 10)
				.until(textToBePresentInElementLocated(By.id("lancesDados"), usuario));

		return temUsuario 
				? driver.getPageSource().contains(String.valueOf(valor)) 
				: false;
	}
	
}
