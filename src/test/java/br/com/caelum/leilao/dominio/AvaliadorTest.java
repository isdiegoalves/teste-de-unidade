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

public class AvaliadorTest {
	
	private Usuario jose;
	private Usuario joao;
	private Usuario maria;
	private CurrencyUnit real;
	private Leilao leilao;

	@Before
	public void before() {
		jose  = new Usuario("jose");
		joao  = new Usuario("joao");
		maria = new Usuario("maria");
		real = Monetary.getCurrency("BRL");
		leilao = new Leilao("Playstation 4", real);
	}

	@Test
	public void cincoLancesEmOrdemCrescenteEMedia() {
		leilao.propoe(new Lance(jose,  Money.of(250, real)));
		leilao.propoe(new Lance(joao,  Money.of(300, real)));
		leilao.propoe(new Lance(maria, Money.of(400, real)));
		leilao.propoe(new Lance(maria, Money.of(900, real)));
		leilao.propoe(new Lance(maria, Money.of(1000, real)));
		
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		MonetaryAmount maiorEsperado = Money.of(1000, real);
		MonetaryAmount menorEsperado = Money.of(250, real);
		MonetaryAmount mediaEsperada = Money.of(570, real);
		
		assertEquals(maiorEsperado, leiloeiro.getMaiorLance());
		assertEquals(menorEsperado, leiloeiro.getMenorLance());
		assertEquals(mediaEsperada, leiloeiro.getMediaLances());
	}
	
	@Test
	public void cincoLancesEmOrdemDecrescente() {
		leilao.propoe(new Lance(maria, Money.of(1000, real)));
		leilao.propoe(new Lance(maria, Money.of(900, real)));
		leilao.propoe(new Lance(maria, Money.of(400, real)));
		leilao.propoe(new Lance(joao,  Money.of(300, real)));
		leilao.propoe(new Lance(jose,  Money.of(250, real)));
		
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		MonetaryAmount maiorEsperado = Money.of(1000, real);
		MonetaryAmount menorEsperado = Money.of(250, real);
		
		assertEquals(maiorEsperado, leiloeiro.getMaiorLance());
		assertEquals(menorEsperado, leiloeiro.getMenorLance());
	}

	@Test
	public void apenasUmLance() {
		leilao.propoe(new Lance(jose,  Money.of(250, real)));
		
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		MonetaryAmount maiorEsperado = Money.of(250, real);
		MonetaryAmount menorEsperado = Money.of(250, real);
		MonetaryAmount mediaEsperada = Money.of(250, real);
		
		assertEquals(maiorEsperado, leiloeiro.getMaiorLance());
		assertEquals(menorEsperado, leiloeiro.getMenorLance());
		assertEquals(mediaEsperada, leiloeiro.getMediaLances());

		List<MonetaryAmount> maiores = leiloeiro.getMaioresValores();
		assertFalse(maiores.isEmpty());
		assertEquals(1, maiores.size());
		assertEquals(Money.of(250, real), maiores.get(0));
	}
	
	@Test
	public void tresMaioresLances() {
		leilao.propoe(new Lance(jose,  Money.of(250, real)));
		leilao.propoe(new Lance(maria, Money.of(1000, real)));
		leilao.propoe(new Lance(maria, Money.of(400, real)));
		leilao.propoe(new Lance(joao,  Money.of(300, real)));
		leilao.propoe(new Lance(maria, Money.of(900, real)));
		
		Avaliador leiloeiro = new Avaliador();
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
		leilao.propoe(new Lance(joao,   Money.of(250, real)));
		leilao.propoe(new Lance(maria,  Money.of(250, real)));
		leilao.propoe(new Lance(jose,   Money.of(250, real)));
		
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		MonetaryAmount maiorEsperado = Money.of(250, real);
		MonetaryAmount menorEsperado = Money.of(250, real);
		MonetaryAmount mediaEsperada = Money.of(250, real);
		
		assertEquals(maiorEsperado, leiloeiro.getMaiorLance());
		assertEquals(menorEsperado, leiloeiro.getMenorLance());
		assertEquals(mediaEsperada, leiloeiro.getMediaLances());

		List<MonetaryAmount> maiores = leiloeiro.getMaioresValores();
		assertFalse(maiores.isEmpty());
		assertEquals(1, maiores.size());
		assertEquals(Money.of(250, real), maiores.get(0));
	}
	
	@Test
	public void leilaoSemLance() {
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		assertEquals(zero(real), leiloeiro.getMaiorLance());
		assertEquals(zero(real), leiloeiro.getMenorLance());
		assertEquals(zero(real), leiloeiro.getMediaLances());
		assertTrue(leiloeiro.getMaioresValores().isEmpty());
	}
	
	@Test(expected=NullPointerException.class)
	public void leilaoComLanceComDinheiroNulo() {
		leilao.propoe(new Lance(joao,   null));
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		assertEquals(zero(real), leiloeiro.getMaiorLance());
		assertEquals(zero(real), leiloeiro.getMenorLance());
		assertEquals(zero(real), leiloeiro.getMediaLances());
		assertTrue(leiloeiro.getMaioresValores().isEmpty());
	}
}
