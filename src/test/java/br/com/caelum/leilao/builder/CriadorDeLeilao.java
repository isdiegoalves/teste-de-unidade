
package br.com.caelum.leilao.builder;

import static br.com.caelum.leilao.dominio.Leilao.leilao;
import static java.time.LocalDate.now;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class CriadorDeLeilao {

	private Leilao leilao;
	
	private CriadorDeLeilao(String descricao, LocalDate dataInicio, CurrencyUnit moedaParaLance) {
		this.leilao = leilao(descricao, dataInicio, moedaParaLance);
	}
	
	public static CriadorDeLeilao para(String descricao, CurrencyUnit moeda) {
		requireNonNull(descricao, "usuario nao pode ser nulo!");
		requireNonNull(moeda, "valor nao pode ser nulo!");
		return para(descricao, now(), moeda);
	}

	public static CriadorDeLeilao para(String descricao, LocalDate data, CurrencyUnit moeda) {
		requireNonNull(descricao, "usuario nao pode ser nulo!");
		requireNonNull(data, "data nao pode ser nulo!");
		requireNonNull(moeda, "valor nao pode ser nulo!");
		return new CriadorDeLeilao(descricao, data, moeda);
	}

	public CriadorDeLeilao lance(Usuario usuario, MonetaryAmount valor) {
		leilao.propor(Lance.lance(usuario,  valor));
		return this;
	}

	public CriadorDeLeilao dobrarLance(Usuario usuario) {
		leilao.dobrarLance(usuario);
		return this;
	}

	public Leilao create() {
		return leilao;
	}
}