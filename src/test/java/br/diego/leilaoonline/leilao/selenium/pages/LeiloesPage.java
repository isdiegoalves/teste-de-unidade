package br.diego.leilaoonline.leilao.selenium.pages;

import static br.diego.leilaoonline.infra.App.urlBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.diego.leilaoonline.lance.selenium.pages.LancesPage;

public class LeiloesPage {

	private final WebDriver driver;

	public LeiloesPage(WebDriver driver) {
		this.driver = driver;
	}

	public void visita() {
		driver.get(urlBase()+"/leiloes");
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

	public LancesPage exibir(int posicao) {
		driver.findElements(By.linkText("exibir")).get(posicao -1).click();
		return new LancesPage(driver);
	}

}
