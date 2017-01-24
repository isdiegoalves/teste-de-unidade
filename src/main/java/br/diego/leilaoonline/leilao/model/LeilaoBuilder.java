package br.diego.leilaoonline.leilao.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LeilaoBuilder {

	private Long id;
	private String descricao;
	private LocalDate dataAbertura = LocalDate.now();
	private List<Lance> lances = new ArrayList<>();
	private CurrencyUnit moeda = Monetary.getCurrency("BRL");
	private boolean encerrado;
	private MonetaryAmount valorInicial;
	private Usuario dono;
	private boolean usado;
	
	public LeilaoBuilder dono(Usuario dono) {
		this.dono = dono;
		return this;
	}
	
	public LeilaoBuilder usado(boolean usado) {
		this.usado = usado;
		return this;
	}
	
	public LeilaoBuilder valorInicial(MonetaryAmount valorInicial) {
		this.valorInicial = valorInicial;
		return this;
	}

	public LeilaoBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public LeilaoBuilder descricao(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public LeilaoBuilder dataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
		return this;
	}

	public LeilaoBuilder lance(Lance lance) {
		this.lances.add(lance);
		return this;
	}
	
	public LeilaoBuilder lances(Collection<Lance> lances) {
		this.lances.addAll(lances);
		return this;
	}

	public LeilaoBuilder moedaParaLance(CurrencyUnit moeda) {
		this.moeda = moeda;
		return this;
	}
	
	public LeilaoBuilder encerrado(boolean encerrado) {
		this.encerrado = encerrado;
		return this;
	}

	public Leilao create() {
		requireNonNull(descricao, "descricao nao pode ser nulo!");
		requireNonNull(dataAbertura, "dataAbertura nao pode ser nulo!");
		requireNonNull(moeda, "moeda nao pode ser nulo!");
		return new Leilao(this);
	}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public LocalDate getDataAbertura() {
		return dataAbertura;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public CurrencyUnit getMoeda() {
		return moeda;
	}

	public boolean isEncerrado() {
		return encerrado;
	}

	public MonetaryAmount getValorInicial() {
		return valorInicial;
	}

	public Usuario getDono() {
		return dono;
	}

	public Boolean isUsado() {
		return usado;
	}
}