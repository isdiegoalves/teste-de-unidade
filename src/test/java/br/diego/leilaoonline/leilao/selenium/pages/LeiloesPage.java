package br.diego.leilaoonline.leilao.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LeiloesPage {

	private final WebDriver driver;

	public LeiloesPage(WebDriver driver) {
		this.driver = driver;
	}

	public void visita() {
		driver.get("http://localhost:8080/leiloes");
	}

	public NovoLeilaoPage novo() {
		driver.findElement(By.linkText("Novo Leilão")).click();
		return new NovoLeilaoPage(driver);
	}
	
	public boolean existeNaListagem(String produto, double valor, boolean usado) {
		return driver.getPageSource().contains(produto) && 
                driver.getPageSource().contains(String.valueOf(valor)) &&
                driver.getPageSource().contains(usado ? "Sim" : "Não");
	}

}
