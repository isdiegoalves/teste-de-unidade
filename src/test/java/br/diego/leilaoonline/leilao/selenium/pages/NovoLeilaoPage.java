package br.diego.leilaoonline.leilao.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class NovoLeilaoPage {

	private final WebDriver driver;

	public NovoLeilaoPage(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver cadastra(String nome, double valor, String usuario, boolean usado) {
		
		WebElement txtNome = driver.findElement(By.name("leilao.nome"));
		txtNome.sendKeys(nome);
		
		driver.findElement(By.name("leilao.valorInicial"))
		.sendKeys(String.valueOf(valor));
		
		new Select(driver.findElement(By.name("leilao.usuario.id")))
		.selectByVisibleText(usuario);		
		
		if (usado) {
			driver.findElement(By.name("leilao.usado"))
			.click();
		}
		
		txtNome.submit();
		
		return driver;
	}
}