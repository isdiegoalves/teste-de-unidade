package br.diego.leilaoonline.usuario.selenium.pages;

import static br.diego.leilaoonline.infra.App.urlBase;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UsuariosPage {

	private final WebDriver driver; 

	public UsuariosPage(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public void visita() {
		driver.get(urlBase()+"/usuarios");
	}

	public NovoUsuarioPage novo() {
		driver.findElement(By.linkText("Novo Usuário")).click();
		return new NovoUsuarioPage(driver);
	}
	
	public AlteraUsuarioPage altera(int posicao) {
		driver.findElements(By.linkText("editar")).get(posicao-1).click();
		return new AlteraUsuarioPage(driver);
	}

	public boolean existeNaListagem(String nome, String email) {
		return driver.getPageSource().contains(nome) &&
				driver.getPageSource().contains(email);
	}

	public void deletaUsuarioNaPosicao(int posicao) {
		driver.findElements(By.tagName("button")).get(posicao-1).click();
		// pega o alert que está aberto
		Alert alert = driver.switchTo().alert();
		// confirma
		alert.accept();
	}
}