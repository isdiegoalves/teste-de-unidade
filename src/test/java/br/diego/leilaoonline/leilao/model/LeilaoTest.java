package br.diego.leilaoonline.leilao.model;

import static br.diego.leilaoonline.lance.model.Lance.lance;
import static br.diego.leilaoonline.leilao.LeilaoMatcher.temUmLance;
import static br.diego.leilaoonline.usuario.model.Usuario.usuario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.leilao.builder.CriadorDeLeilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LeilaoTest {

	private Usuario isabela;
	private Usuario diego;
	private CurrencyUnit real;

	@Before
	public void setUp() {
		diego =   usuario("Diego");
		isabela = usuario("Isabela");
		real = Monetary.getCurrency("BRL");
	}

	@Test
	public void deveAceitarApenasUmLance() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
			.lance(diego, Money.of(250, real))
			.create();
		
		assertThat(leilao.getLances(), hasSize(1));
		assertThat(leilao.getLances(), hasItem(lance(diego, Money.of(250, real), leilao)));
	}

	@Test
	public void deveAceitarDoisLances() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
			.lance(diego,   Money.of(250, real))
			.lance(isabela, Money.of(450, real))
			.create();

		assertThat(leilao.getLances(), hasSize(2));
		assertThat(leilao.getLances(), hasItems(
				lance(diego,   Money.of(250, real), leilao),
				lance(isabela, Money.of(450, real), leilao)));
	}

	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmosUsuario() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
				.lance(diego, Money.of(250, real))
				.lance(diego, Money.of(300, real))
				.create();

		assertThat(leilao.getLances(), hasSize(1));
		assertThat(leilao.getLances(), hasItem(lance(diego, Money.of(250, real), leilao)));
	}

	@Test
	public void naoDeveAceitarMaisDoQueCincoLancesDoMesmosUsuario() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
			.lance(diego,   Money.of(250, real))//1
			.lance(isabela, Money.of(300, real))
			.lance(diego,   Money.of(350, real))//2
			.lance(isabela, Money.of(400, real))
			.lance(diego,   Money.of(450, real))//3
			.lance(isabela, Money.of(500, real))
			.lance(diego,   Money.of(550, real))//4
			.lance(isabela, Money.of(600, real))
			.lance(diego,   Money.of(650, real))//5
			.lance(isabela, Money.of(700, real))
			.lance(diego,   Money.of(750, real))//6, nao deve aceita esse lance
			.create();

		assertThat(leilao.getLances(), hasSize(10));
		int ultimo = leilao.getLances().size()-1;
		assertThat(leilao.getLances().get(ultimo), equalTo(lance(isabela, Money.of(700, real), leilao)));
	}

	@Test
	public void deveAceitarApostaDobradaUltimoLance() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
				.lance(diego,   Money.of(250, real))//1
				.lance(isabela, Money.of(300, real))
				.lance(diego,   Money.of(350, real))//2
				.lance(isabela, Money.of(400, real))
				.lance(diego,   Money.of(450, real))//3
				.lance(isabela, Money.of(500, real))
				.dobrarLance(diego)//4
				.dobrarLance(isabela)
				.dobrarLance(diego)//5
				.create();

		assertThat(leilao.getLances(), hasSize(9));
		int ultimo = leilao.getLances().size()-1;
		assertThat(leilao.getLances().get(ultimo), equalTo(lance(diego, Money.of(1800, real), leilao)));
		//assertThat(leilao.getLances().get(ultimo), equalTo(lance(diego, Money.of(4000, real))));
	}
	
	@Test
	public void naoDeveAceitarApostaDobradaSemExistirLance() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
				.dobrarLance(diego)
				.create();

		assertThat(leilao.getLances(), is(empty()));
	}
	
	@Test
	public void naoDeveAceitarApostaDobradaNoSextoLanceDoMesmosUsuario() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
				.lance(diego,   Money.of(250, real))//1
				.lance(isabela, Money.of(300, real))
				.lance(diego,   Money.of(350, real))//2
				.lance(isabela, Money.of(400, real))
				.lance(diego,   Money.of(450, real))//3
				.lance(isabela, Money.of(500, real))
				.lance(diego,   Money.of(550, real))//4
				.lance(isabela, Money.of(600, real))
				.lance(diego,   Money.of(700, real))//5
				.lance(isabela, Money.of(800, real))
				.dobrarLance(diego)//6, nao deve aceitar o sexto lance
				.create();

		assertThat(leilao.getLances(), hasSize(10));
		int ultimo = leilao.getLances().size()-1;
		assertThat(leilao.getLances().get(ultimo), equalTo(lance(isabela,Money.of(800, real), leilao)));
	}
	
	@Test
	public void naoDeveAceitarApostaDobradaNoSextoLanceDobradoDoMesmosUsuario() {
		Leilao leilao = CriadorDeLeilao.para("Playstation", real)
				.lance(diego,   Money.of(250, real))//1
				.lance(isabela, Money.of(300, real))
				.lance(diego,   Money.of(350, real))//2
				.lance(isabela, Money.of(400, real))
				.lance(diego,   Money.of(450, real))//3
				.lance(isabela, Money.of(500, real))
				.dobrarLance(diego)//4
				.lance(isabela, Money.of(1500, real))
				.dobrarLance(diego)//5
				.dobrarLance(diego)//6, ignorado, aceita somente 5 lances do mesmo usuario
				.dobrarLance(diego)//7, ignorado, aceita somente 5 lances do mesmo usuario
				.create();

		assertThat(leilao.getLances(), hasSize(9));
		int ultimo = leilao.getLances().size()-1;
		assertThat(leilao.getLances().get(ultimo), equalTo(lance(diego, Money.of(1800, real), leilao)));
		//assertThat(leilao.getLances().get(ultimo), equalTo(lance(diego, Money.of(3000, real))));
	}
	
	@Test
	public void naoDeveAceitarLanceMenorQueOAnterior() {
		Leilao leilao = CriadorDeLeilao.para("Playstation 3", real)
				.lance(diego,   Money.of(250, real))
				.lance(isabela, Money.of(249, real))
				.lance(diego,   Money.of(200, real))
				.lance(isabela, Money.of(150, real))
				.lance(diego,   Money.of(10, real))
				.create();

		List<Lance> lances = leilao.getLances();
		assertThat(lances, hasSize(1));
		assertThat(lances.get(0).getValor(), equalTo(Money.of(250, real)));
	}
	
	@Test(expected=NullPointerException.class)
	public void naoDeveAceitarLeilaoSemDescricao() {
		CriadorDeLeilao.para(null, real)
		.create();
	}
	
	@Test(expected=NullPointerException.class)
	public void naoDeveAceitarLeilaoSemMoedaDefinida() {
		CriadorDeLeilao.para("Playstation 3", null)
		.create();
	}
	
	@Test
	public void deveReceberUmLance() {
		Leilao leilao = CriadorDeLeilao.para("Macbook Pro 15", real).create();

		Lance lance = lance(diego, Money.of(2000, real), leilao);
		leilao.propor(lance);

		assertThat(leilao.getLances(), hasSize(1));
		assertThat(leilao, temUmLance(lance));
	}
}
