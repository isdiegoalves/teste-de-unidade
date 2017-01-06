package br.com.caelum.leilao.dominio;

import static br.com.caelum.leilao.dominio.Lance.lance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

public class LanceTest {

	private Usuario joao;
	private CurrencyUnit real;
	
	@Before
	public void setUp() {
		joao  = new Usuario("joao");
		real = Monetary.getCurrency("BRL");
	}
	
	@Test
	public void devePermitirLanceComUsuarioEValorDoLance() {
		Lance lance = lance(joao,  Money.of(1000, real));
		
		assertThat(lance.getUsuario(), equalTo(joao));
		assertThat(lance.getValor(), equalTo(Money.of(1000, real)));
	}
	
	@Test(expected=NullPointerException.class)
	public void naoPermiteLanceSemValor() {
		lance(joao,  null);
	}
	
	@Test(expected=NullPointerException.class)
	public void naoPermiteLanceSemUsuario() {
		lance(null,  Money.of(1000, real));
	}
}