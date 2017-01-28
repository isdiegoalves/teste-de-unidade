package br.diego.leilaoonline.lance.selenium;

import static br.diego.leilaoonline.infra.selenium.WebDrivers.chromeWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import br.diego.leilaoonline.infra.selenium.CriadorDeCenarios;
import br.diego.leilaoonline.lance.selenium.pages.LancesPage;
import br.diego.leilaoonline.leilao.selenium.pages.LeiloesPage;

public class LanceSystemTest {

    private WebDriver driver;
    private LeiloesPage leiloes;

    @Before
    public void before() {
        this.driver = chromeWebDriver();
        this.leiloes = new LeiloesPage(driver);
        
        CriadorDeCenarios.novoCenario(driver)
        .umUsuario("Diego", "diego@alves.com")
        .umUsuario("Camila", "camila@estefani.com")
        .umLeilao("Camila", "Geladeira", 1000, false);
    }
    
    @After
    public void after() {
    	driver.quit();
    }

    @Test
    public void deveFazerUmLance() {

        LancesPage lances = leiloes.exibir(1);

        lances.lance("Diego", 150);

        assertThat(lances.existeLance("Diego", 150), is(true));
    }

}