package br.com.caelum.leilao.dominio;

import static org.javamoney.moneta.FastMoney.zero;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.LeilaoBuilder;

public class AvaliadorTest {

	private Usuario jose;
	private Usuario joao;
	private Usuario maria;
	private CurrencyUnit real;
	private Avaliador leiloeiro;

	@Before
	public void before() {
		leiloeiro = new Avaliador();
		jose  = new Usuario("jose");
		joao  = new Usuario("joao");
		maria = new Usuario("maria");
		real = Monetary.getCurrency("BRL");
	}

	@Test
	public void cincoLancesEmOrdemCrescenteEMedia() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real)
				.lance(jose,  250)
				.lance(joao,  300)
				.lance(maria, 400)
				.lance(jose,  900)
				.lance(maria, 1000)
				.create();

		leiloeiro.avalia(leilao);

		assertEquals(Money.of(1000, real), leiloeiro.getMaiorLance());
		assertEquals(Money.of(250, real),  leiloeiro.getMenorLance());
		assertEquals(Money.of(570, real),  leiloeiro.getMediaLances());
	}

	@Test
	public void cincoLancesEmOrdemDecrescente() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real)
				.lance(maria, 1000)
				.lance(jose,  900)
				.lance(maria, 400)
				.lance(joao,  300)
				.lance(jose,  250)
				.create();

		leiloeiro.avalia(leilao);

		assertEquals(Money.of(1000, real), leiloeiro.getMaiorLance());
		assertEquals(Money.of(250, real),  leiloeiro.getMenorLance());
	}

	@Test
	public void apenasUmLance() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real)
				.lance(jose, 250)
				.create();

		leiloeiro.avalia(leilao);

		assertEquals(Money.of(250, real), leiloeiro.getMaiorLance());
		assertEquals(Money.of(250, real), leiloeiro.getMenorLance());
		assertEquals(Money.of(250, real), leiloeiro.getMediaLances());

		List<MonetaryAmount> maiores = leiloeiro.getMaioresValores();
		assertFalse(maiores.isEmpty());
		assertEquals(1, maiores.size());
		assertEquals(Money.of(250, real), maiores.get(0));
	}

	@Test
	public void tresMaioresLances() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real)
				.lance(jose,  250)
				.lance(maria, 1000)
				.lance(jose,  400)
				.lance(joao,  300)
				.lance(maria, 900)
				.create();

		leiloeiro.avalia(leilao);

		List<MonetaryAmount> maiores = leiloeiro.getMaioresValores();
		assertFalse(maiores.isEmpty());
		assertEquals(3, maiores.size());
		assertEquals(Money.of(1000, real), maiores.get(0));
		assertEquals(Money.of(900, real), maiores.get(1));
		assertEquals(Money.of(400, real), maiores.get(2));

	}


	@Test
	public void cincoLancesComValoresIguais() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real)
				.lance(joao,  250)
				.lance(maria, 250)
				.lance(jose,  250)
				.create();

		leiloeiro.avalia(leilao);

		assertEquals(Money.of(250, real), leiloeiro.getMaiorLance());
		assertEquals(Money.of(250, real), leiloeiro.getMenorLance());
		assertEquals(Money.of(250, real), leiloeiro.getMediaLances());

		List<MonetaryAmount> maiores = leiloeiro.getMaioresValores();
		assertFalse(maiores.isEmpty());
		assertEquals(1, maiores.size());
		assertEquals(Money.of(250, real), maiores.get(0));
	}

	@Test
	public void leilaoSemLance() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real).create();

		leiloeiro.avalia(leilao);

		assertEquals(zero(real), leiloeiro.getMaiorLance());
		assertEquals(zero(real), leiloeiro.getMenorLance());
		assertEquals(zero(real), leiloeiro.getMediaLances());
		assertTrue(leiloeiro.getMaioresValores().isEmpty());
	}

	@Test(expected=NullPointerException.class)
	public void leilaoComLanceComDinheiroNulo() {
		Leilao leilao = LeilaoBuilder.of("Playstation 3", real)
				.lance(joao, null)
				.create();

		leiloeiro.avalia(leilao);

		assertEquals(zero(real), leiloeiro.getMaiorLance());
		assertEquals(zero(real), leiloeiro.getMenorLance());
		assertEquals(zero(real), leiloeiro.getMediaLances());
		assertTrue(leiloeiro.getMaioresValores().isEmpty());
	}
}