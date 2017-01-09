package br.com.caelum.leilao.dominio;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.money.CurrencyUnit;

public class LeilaoBuilder {

	private int id;
	private String descricao;
	private LocalDate dataInicio;
	private List<Lance> lances = new ArrayList<>();
	private CurrencyUnit moeda;
	private AtomicBoolean encerrado = new AtomicBoolean(false);

	public LeilaoBuilder id(int id) {
		this.id = id;
		return this;
	}

	public LeilaoBuilder descricao(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public LeilaoBuilder dataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
		return this;
	}

	public LeilaoBuilder lance(Lance lance) {
		this.lances.add(lance);
		return this;
	}

	public LeilaoBuilder moedaParaLance(CurrencyUnit moeda) {
		this.moeda = moeda;
		return this;
	}
	
	public LeilaoBuilder encerrado(boolean encerrado) {
		this.encerrado = new AtomicBoolean(encerrado);
		return this;
	}

	public Leilao create() {
		requireNonNull(descricao, "descricao nao pode ser nulo!");
		requireNonNull(dataInicio, "dataInicio nao pode ser nulo!");
		requireNonNull(moeda, "valor nao pode ser nulo!");
		return new Leilao(this);
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public CurrencyUnit getMoeda() {
		return moeda;
	}

	public AtomicBoolean getEncerrado() {
		return encerrado;
	}
}