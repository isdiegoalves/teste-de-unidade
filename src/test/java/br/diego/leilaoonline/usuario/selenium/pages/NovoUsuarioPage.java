package br.diego.leilaoonline.usuario.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NovoUsuarioPage {

WebDriver driver; 
	
	public NovoUsuarioPage(WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public void cadastra(String nome, String email) {
		driver.findElement(By.name("usuario.nome")).sendKeys(nome);
		
		driver.findElement(By.name("usuario.email")).sendKeys(email);
		
		driver.findElement(By.id("btnSalvar")).click();
	}
}
