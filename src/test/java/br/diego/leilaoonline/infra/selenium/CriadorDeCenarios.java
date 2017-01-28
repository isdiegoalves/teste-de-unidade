package br.diego.leilaoonline.infra.selenium;

import static br.diego.leilaoonline.infra.App.urlBase;

import org.openqa.selenium.WebDriver;

import br.diego.leilaoonline.leilao.selenium.pages.LeiloesPage;
import br.diego.leilaoonline.usuario.selenium.pages.UsuariosPage;

public class CriadorDeCenarios {

    private WebDriver driver;

    public CriadorDeCenarios(WebDriver driver) {
        this.driver = driver;
    }

    public CriadorDeCenarios umUsuario(String nome, String email) {
        UsuariosPage usuarios = new UsuariosPage(driver);
        usuarios.visita();
        usuarios.novo().cadastra(nome, email);

        return this;
    }

    public CriadorDeCenarios umLeilao(String usuario, 
                String produto, 
                double valor, 
                boolean usado) {
        LeiloesPage leiloes = new LeiloesPage(driver);
        leiloes.visita();
        leiloes.novo().cadastra(produto, valor, usuario, usado);

        return this;
    }

	public static CriadorDeCenarios novoCenario(WebDriver driver) {
		driver.get(urlBase()+"/apenas-teste/limpa");
		return new CriadorDeCenarios(driver);
	}

}