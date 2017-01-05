
package br.com.caelum.leilao.builder;

import javax.money.CurrencyUnit;

import org.javamoney.moneta.Money;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class LeilaoBuilder {

	private Leilao leilao;
	
	private LeilaoBuilder(String descricao, CurrencyUnit moeda) {
		this.leilao = new Leilao(descricao, moeda);
	}

	public static LeilaoBuilder of(String descricao, CurrencyUnit moeda) {
		return new LeilaoBuilder(descricao, moeda);
	}

	public LeilaoBuilder lance(Usuario usuario, Number valor) {
		leilao.propoe(new Lance(usuario,  Money.of(valor, leilao.getMoeda())));
		return this;
	}

	public Leilao create() {
		return leilao;
	}

}
