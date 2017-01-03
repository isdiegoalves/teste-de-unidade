package br.com.caelum.leilao.dominio;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

public class AvaliadorTest {

	@Test
	public void maiorEMenorDeTodos() {
		CurrencyUnit REAL = Monetary.getCurrency("BRL");
		Usuario jose = new Usuario("jose");
		Usuario joao = new Usuario("joao");
		Usuario maria = new Usuario("maria");
		
		Leilao leilao = new Leilao("Playstation 4");
		
		leilao.propoe(new Lance(maria, Money.of(400, REAL)));
		leilao.propoe(new Lance(jose,  Money.of(250, REAL)));
		leilao.propoe(new Lance(joao,  Money.of(300, REAL)));
		
		Avaliador leiloeiro = new Avaliador();
		leiloeiro.avalia(leilao);
		
		MonetaryAmount maiorEsperado = Money.of(400, REAL);
		MonetaryAmount menorEsperado = Money.of(250, REAL);
		
		Assert.assertEquals(maiorEsperado, leiloeiro.getMaiorDeTodos());
		Assert.assertEquals(menorEsperado, leiloeiro.getMenorDeTodos());
	}
}
