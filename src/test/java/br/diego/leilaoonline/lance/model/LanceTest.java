package br.diego.leilaoonline.lance.model;

import static br.diego.leilaoonline.lance.model.Lance.lance;
import static br.diego.leilaoonline.usuario.model.Usuario.usuario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LanceTest {

	private Usuario joao;
	private CurrencyUnit real;
	
	@Before
	public void setUp() {
		joao  = usuario("joao");
		real = Monetary.getCurrency("BRL");
	}
	
	@Test
	public void devePermitirLanceComUsuarioEValorDoLance() {
		Lance lance = lance(joao,  Money.of(1000, real), Leilao.builder().descricao("Geladeira")
				.create());
		
		assertThat(lance.getUsuario(), equalTo(joao));
		assertThat(lance.getValor(), equalTo(Money.of(1000, real)));
	}
	
	@Test(expected=NullPointerException.class)
	public void naoDevePermitirLanceSemValor() {
		lance(joao,  null, null);
	}
	
	@Test(expected=NullPointerException.class)
	public void naoDevePermitirLanceSemUsuario() {
		lance(null,  Money.of(1000, real), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void naoDevePermitirLanceSemLanceComValorNegativoOuZero() {
		lance(joao,  Money.of(-1000, real), Leilao.builder().descricao("Geladeira")
				.create());
	}
}
