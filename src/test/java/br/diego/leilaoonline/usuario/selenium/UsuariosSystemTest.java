package br.diego.leilaoonline.usuario.selenium;

import static br.diego.leilaoonline.infra.App.urlBase;
import static br.diego.leilaoonline.infra.selenium.WebDrivers.chromeWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.diego.leilaoonline.usuario.selenium.pages.UsuariosPage;

public class UsuariosSystemTest {

	private WebDriver driver;
	private UsuariosPage usuarios;

	@Before
	public void before() {
		driver = chromeWebDriver();
		driver.get(urlBase()+"/apenas-teste/limpa");
		usuarios = new UsuariosPage(driver);
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

	@Test
	public void deveCadastrarEExcluirUsuario() {

		usuarios.visita();
		usuarios.novo().cadastra("Diego Alves Oliveira da Silva", "isdiegoalves@gmail.com");

		assertThat(usuarios.existeNaListagem("Diego Alves Oliveira da Silva", "isdiegoalves@gmail.com"), is(true));

		int posicao = 1; // queremos o 1o botao da pagina
		driver.findElements(By.tagName("button")).get(posicao-1).click();
		// pega o alert que está aberto
		Alert alert = driver.switchTo().alert();
		// confirma
		alert.accept();
	}
	
	@Test
    public void deveDeletarUmUsuario() {
		usuarios.visita();
        usuarios.novo().cadastra("Ronaldo Luiz de Albuquerque", "ronaldo2009@terra.com.br");
        assertThat(usuarios.existeNaListagem ("Ronaldo Luiz de Albuquerque", "ronaldo2009@terra.com.br"), is(true));

        usuarios.deletaUsuarioNaPosicao(1);

        assertThat(usuarios.existeNaListagem("Ronaldo Luiz de Albuquerque", "ronaldo2009@terra.com.br"), is(false));
    }
	
	@Test
	public void deveEditarUsuarioPrimeiraPosicao() {
		usuarios.visita();
		usuarios.novo().cadastra("Diego Alves", "diego@alves.com");;
		usuarios.altera(1).para("Hulk", "hulk@com");

		assertThat(usuarios.existeNaListagem ("Hulk", "hulk@com"), is(true));
	}
}
