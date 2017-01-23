package br.diego.leilaoonline.leilao.builder;

import static br.diego.leilaoonline.leilao.entity.Leilao.leilao;
import static java.time.LocalDate.now;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import br.diego.leilaoonline.lance.entity.Lance;
import br.diego.leilaoonline.leilao.entity.Leilao;
import br.diego.leilaoonline.usuario.entity.Usuario;

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
		leilao.propor(Lance.lance(usuario,  valor, leilao));
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