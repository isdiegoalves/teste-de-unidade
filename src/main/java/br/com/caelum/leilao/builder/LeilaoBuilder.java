
package br.com.caelum.leilao.builder;

import static java.util.Objects.requireNonNull;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class LeilaoBuilder {

	private Leilao leilao;
	
	private LeilaoBuilder(String descricao, CurrencyUnit moeda) {
		this.leilao = new Leilao(descricao, moeda);
	}

	public static LeilaoBuilder of(String descricao, CurrencyUnit moeda) {
		requireNonNull(descricao, "usuario nao pode ser nulo!");
		requireNonNull(moeda, "valor nao pode ser nulo!");
		return new LeilaoBuilder(descricao, moeda);
	}

	public LeilaoBuilder lance(Usuario usuario, MonetaryAmount valor) {
		leilao.propoe(Lance.lance(usuario,  valor));
		return this;
	}

	public LeilaoBuilder dobrarLance(Usuario usuario) {
		leilao.dobrarLance(usuario);
		return this;
	}

	public Leilao create() {
		return leilao;
	}
}
