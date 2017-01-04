package br.com.caelum.leilao.dominio;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LeilaoTest {

	private Usuario isabela;
	private Usuario diego;

	@Before
	public void before() {
		diego = new Usuario("Diego");
		isabela = new Usuario("Isabela");
	}

	@Test
	public void deveReceberApenasUmLance() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		Assert.assertEquals(1, leilao.getLances().size());

		Assert.assertEquals(Money.of(250, real), leilao.getLances().get(0).getValor());
	}

	@Test
	public void deveReceberVariosLances() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		leilao.propoe(new Lance(isabela, Money.of(450, real)));

		Assert.assertEquals(2, leilao.getLances().size());
		Assert.assertEquals(Money.of(250, real), leilao.getLances().get(0).getValor());
		Assert.assertEquals(Money.of(450, real), leilao.getLances().get(1).getValor());
	}

	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmosUsuario() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		leilao.propoe(new Lance(diego, Money.of(300, real)));

		Assert.assertEquals(1, leilao.getLances().size());
		Assert.assertEquals(Money.of(250, real), leilao.getLances().get(0).getValor());
	}

	@Test
	public void naoDeveReceberMaisDoQueCincoLancesDoMesmosUsuario() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		leilao.propoe(new Lance(isabela, Money.of(300, real)));

		leilao.propoe(new Lance(diego, Money.of(350, real)));
		leilao.propoe(new Lance(isabela, Money.of(400, real)));

		leilao.propoe(new Lance(diego, Money.of(450, real)));
		leilao.propoe(new Lance(isabela, Money.of(500, real)));

		leilao.propoe(new Lance(diego, Money.of(550, real)));
		leilao.propoe(new Lance(isabela, Money.of(600, real)));

		leilao.propoe(new Lance(diego, Money.of(650, real)));
		leilao.propoe(new Lance(isabela, Money.of(700, real)));

		//nao deve aceita esse lance, sexto lance
		leilao.propoe(new Lance(diego, Money.of(750, real)));

		Assert.assertEquals(10, leilao.getLances().size());
		int ultimo = leilao.getLances().size()-1;
		Lance ultimoLance = leilao.getLances().get(ultimo);
		Assert.assertEquals(Money.of(700, real), ultimoLance.getValor());
	}

	@Test
	public void deveReceberApostaDobradaUltimoLance() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		leilao.propoe(new Lance(isabela, Money.of(300, real)));

		leilao.propoe(new Lance(diego, Money.of(350, real)));
		leilao.propoe(new Lance(isabela, Money.of(400, real)));

		leilao.propoe(new Lance(diego, Money.of(450, real)));
		leilao.propoe(new Lance(isabela, Money.of(500, real)));

		leilao.dobrarLance(diego);

		Assert.assertEquals(7, leilao.getLances().size());
		int ultimo = leilao.getLances().size()-1;
		Lance ultimoLance = leilao.getLances().get(ultimo);
		Assert.assertEquals(Money.of(900, real), ultimoLance.getValor());
	}
	
	@Test
	public void naoDeveReceberApostaDobradaSemLance() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.dobrarLance(diego);

		Assert.assertEquals(0, leilao.getLances().size());
	}
	
	@Test
	public void naoDeveReceberApostaDobradaNoSextoLanceDoMesmosUsuario() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		leilao.propoe(new Lance(isabela, Money.of(300, real)));

		leilao.propoe(new Lance(diego, Money.of(350, real)));
		leilao.propoe(new Lance(isabela, Money.of(400, real)));

		leilao.propoe(new Lance(diego, Money.of(450, real)));
		leilao.propoe(new Lance(isabela, Money.of(500, real)));
		
		leilao.propoe(new Lance(diego, Money.of(500, real)));
		leilao.propoe(new Lance(isabela, Money.of(600, real)));

		leilao.propoe(new Lance(diego, Money.of(700, real)));
		leilao.propoe(new Lance(isabela, Money.of(800, real)));

		leilao.dobrarLance(diego);

		Assert.assertEquals(10, leilao.getLances().size());
		int ultimo = leilao.getLances().size()-1;
		Lance ultimoLance = leilao.getLances().get(ultimo);
		Assert.assertEquals(Money.of(800, real), ultimoLance.getValor());
	}
	
	@Test
	public void naoDeveReceberApostaDobradaNoSextoLanceDobradoDoMesmosUsuario() {
		CurrencyUnit real = Monetary.getCurrency("BRL");

		Leilao leilao = new Leilao("Playstation", real);
		Assert.assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(diego, Money.of(250, real)));
		leilao.propoe(new Lance(isabela, Money.of(300, real)));

		leilao.propoe(new Lance(diego, Money.of(350, real)));
		leilao.propoe(new Lance(isabela, Money.of(400, real)));

		leilao.propoe(new Lance(diego, Money.of(450, real)));
		leilao.propoe(new Lance(isabela, Money.of(500, real)));
		
		leilao.dobrarLance(diego);
		leilao.propoe(new Lance(isabela, Money.of(600, real)));

		leilao.dobrarLance(diego);

		leilao.dobrarLance(diego);

		Assert.assertEquals(9, leilao.getLances().size());
		int ultimo = leilao.getLances().size()-1;
		Lance ultimoLance = leilao.getLances().get(ultimo);
		Assert.assertEquals(Money.of(1800, real), ultimoLance.getValor());
	}
}
