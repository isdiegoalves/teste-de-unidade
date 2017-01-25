package br.diego.leilaoonline.usuario.selenium;

import static java.lang.System.setProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.diego.leilaoonline.usuario.selenium.pages.UsuariosPage;

public class UsuariosSystemTest {

	private WebDriver driver;
	private UsuariosPage usuarios;
	
	@Before
	public void before() {
		setProperty("webdriver.chrome.driver", "src/test/resources/selenium/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		usuarios = new UsuariosPage(driver);
	}
	
	@After
	public void after() {
		driver.quit();
	}


	@Test
	public void deveCadastrarUsuario() {
		usuarios.visita();
		usuarios.novo().cadastra("Diego Alves Oliveira da Silva", "isdiegoalves@gmail.com");
		
		assertThat(usuarios.existeNaListagem("Diego Alves Oliveira da Silva", "isdiegoalves@gmail.com"), is(true));
	}
	
	
	@Test
	public void naoDeveCadastrarUsuarioSemNome() {
		usuarios.visita();
		usuarios.novo().cadastra("", "isdiegoalves@gmail.com");
		
		assertThat(driver.getPageSource(), containsString("Nome obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));
	}
	
	@Test
	public void naoDeveCadastrarUsuarioSemEmail() {
		usuarios.visita();
		usuarios.novo().cadastra("Diego Alves Oliveira da Silva", "");
		
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
