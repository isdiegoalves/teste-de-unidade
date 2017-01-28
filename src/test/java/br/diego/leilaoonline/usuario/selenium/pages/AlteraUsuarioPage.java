package br.diego.leilaoonline.usuario.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AlteraUsuarioPage {
	
private final WebDriver driver; 
	
	public AlteraUsuarioPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void para(String nome, String email) {
		driver.findElement(By.name("usuario.nome")).clear();
		driver.findElement(By.name("usuario.nome")).sendKeys(nome);
		
		driver.findElement(By.name("usuario.email")).clear();
		driver.findElement(By.name("usuario.email")).sendKeys(email);
		
		driver.findElement(By.id("btnSalvar")).click();
	}
	
}

