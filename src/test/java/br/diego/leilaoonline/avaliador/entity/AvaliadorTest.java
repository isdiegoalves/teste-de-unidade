package br.diego.leilaoonline.avaliador.entity;

import static br.diego.leilaoonline.lance.entity.Lance.lance;
import static br.diego.leilaoonline.usuario.entity.Usuario.usuario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import br.diego.leilaoonline.lance.entity.Lance;
import br.diego.leilaoonline.leilao.builder.CriadorDeLeilao;
import br.diego.leilaoonline.leilao.entity.Leilao;
import br.diego.leilaoonline.usuario.entity.Usuario;

public class AvaliadorTest {

	private Usuario jose;
	private Usuario joao;
	private Usuario maria;
	private CurrencyUnit real;
	private Avaliador leiloeiro;
	
	@Before
	public void setUp() {
		leiloeiro = new Avaliador();
		jose  = usuario("jose");
		joao  = usuario("joao");
		maria = usuario("maria");
		real = Monetary.getCurrency("BRL");
	}

	@Test
	public void deveAvaliarOMaiorOMenorEMediaLances() {
		Leilao leilao = CriadorDeLeilao.para("Playstation 3", real)
				.lance(jose,  Money.of(250, real))
				.lance(joao,  Money.of(300, real))
				.lance(maria, Money.of(400, real))
				.lance(jose,  Money.of(900, real))
				.lance(maria, Money.of(1000, real))
				.create();

		leiloeiro.avalia(leilao);

		assertThat(leiloeiro.getMaiorLance(), equalTo(Money.of(1000, real)));
		assertThat(leiloeiro.getMenorLance(), equalTo(Money.of(250, real)));
		assertThat(leiloeiro.getMediaLances(), equalTo(Money.of(570, real)));
	}

	@Test
	public void deveAvaliarOsTresMaioresLancesEmOrdemCrescente() {
		Leilao leilao = CriadorDeLeilao.para("Playstation 3", real)
				.lance(jose,  Money.of(250, real))
				.lance(joao,  Money.of(300, real))
				.lance(jose,  Money.of(400, real))
				.lance(maria, Money.of(900, real))
				.lance(joao, Money.of(1000, real))
				.create();

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getMaioresLances();
		assertThat(maiores.size(), is(3));
		assertThat(maiores, hasItems(
				lance(joao, Money.of(1000, real), leilao),
				lance(maria, Money.of(900, real), leilao),
				lance(jose,  Money.of(400, real), leilao)));
	}
	
	@Test
	public void deveAvaliarLancesEmOrdemDecrescenteEAceitarSomenteOPrimeiro() {
		Leilao leilao = CriadorDeLeilao.para("Playstation 3", real)
				.lance(joao, Money.of(1000, real))
				.lance(maria, Money.of(900, real))//ignorado, nao eh permitido lance menor que o anterior
				.lance(jose,  Money.of(400, real))//ignorado, nao eh permitido lance menor que o anterior
				.lance(joao,  Money.of(300, real))//ignorado, nao eh permitido lance menor que o anterior
				.lance(jose,  Money.of(250, real))//ignorado, nao eh permitido lance menor que o anterior
				.create();

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getMaioresLances();
		assertThat(maiores.size(), is(1));
		assertThat(maiores, hasItems(lance(joao, Money.of(1000, real), leilao)));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void naoDeveAvaliarLeilaoSemLanceDado() {
		Leilao leilao = CriadorDeLeilao.para("Playstation 3", real).create();

		leiloeiro.avalia(leilao);
	}
}