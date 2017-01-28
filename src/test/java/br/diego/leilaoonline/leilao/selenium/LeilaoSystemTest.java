package br.diego.leilaoonline.leilao.selenium;

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

import br.diego.leilaoonline.leilao.selenium.pages.LeiloesPage;

public class LeilaoSystemTest {

	private WebDriver driver;
	private LeiloesPage leiloes;

	@Before
	public void before() {
		driver = chromeWebDriver();
		driver.get("http://localhost:8080/apenas-teste/limpa");
		leiloes = new LeiloesPage(driver);
	}

	@After
	public void after() {
		driver.quit();
	}


	@Test
	public void deveCadastrarLeilao() {
		leiloes.visita();
		leiloes.novo().cadastra("Playstation", 1000, "Diego", true);
		
		assertThat(leiloes.existeNaListagem("Playstation", 1000, true), is(true));
	}
	

	@Test
	public void naoDeveCadastrarLeilaoSemNome() {
		leiloes.visita();
		leiloes.novo().cadastra("", 1000, "Diego", true);

		assertThat(driver.getPageSource(), containsString("Nome obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));
	}
	
	@Test
	public void naoDeveCadastrarLeilaoSemValorInicial() {
		leiloes.visita();
		leiloes.novo().cadastra("Playstation", 0, "Diego", true);

		assertThat(driver.getPageSource(), containsString("Valor inicial deve ser maior que zero!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));
	}

	public void naoDeveCadastrarUsuarioSemEmailESemEmail() {
		leiloes.visita();
		leiloes.novo().cadastra("Playstation", 1000, "Diego", true);

		assertThat(driver.getPageSource(), containsString("Nome obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("E-mail obrigatorio!"));
		assertThat(driver.getPageSource(), containsString("Salvar!"));

	}

	public void deveCadastrarEExcluirUsuario() {

		leiloes.visita();
		leiloes.novo().cadastra("Playstation", 1000, "Diego", true);

		assertThat(leiloes.existeNaListagem("Playstation", 1000, true), is(true));

		int posicao = 1; // queremos o 1o botao da pagina
		driver.findElements(By.tagName("button")).get(posicao-1).click();
		// pega o alert que está aberto
		Alert alert = driver.switchTo().alert();
		// confirma
		alert.accept();
	}
	
}