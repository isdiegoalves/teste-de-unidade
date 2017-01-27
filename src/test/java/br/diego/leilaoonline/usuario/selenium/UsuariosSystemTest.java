package br.diego.leilaoonline.usuario.selenium;

import static br.diego.leilaoonline.infra.selenium.WebDrivers.chromeWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import br.diego.leilaoonline.usuario.selenium.pages.UsuariosPage;

public class UsuariosSystemTest {

	private WebDriver driver;
	private UsuariosPage usuarios;
	
	@Before
	public void before() {
		usuarios = new UsuariosPage(chromeWebDriver());
	}
	
	@After
	public void after() {
		driver.quit();
	}


	@Test
	public void deveCadastrarUsuario() {
		usuarios.visita();
		usuarios.novo().cadastra("Diego Alves", "diego@alves.com");
		
		assertThat(usuarios.existeNaListagem("Diego Alves", "diego@alves.com"), is(true));
	}
	
	
	@Test
	public void naoDeveCadastrarUsuarioSemNome() {
		usuarios.visita();
		usuarios.novo().cadastra("", "diego@alves.com");
		
		assertThat(driver.getPageSource(), containsString("Nome obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));
	}
	
	@Test
	public void naoDeveCadastrarUsuarioSemEmail() {
		usuarios.visita();
		usuarios.novo().cadastra("Diego Alves", "");
		
		assertThat(driver.getPageSource(), containsString("E-mail obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));
	}
	
	@Test
	public void naoDeveCadastrarUsuarioSemEmailESemEmail() {
		usuarios.visita();
		usuarios.novo().cadastra("", "");
		
		assertThat(driver.getPageSource(), containsString("Nome obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("E-mail obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));

	}
}
