package br.com.caelum.leilao.dominio;

import static br.com.caelum.leilao.dominio.Lance.lance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.money.CurrencyUnit;

public class Leilao {

	private final int id;
	private final String descricao;
	private final LocalDate data;
	private final List<Lance> lances;
	private final CurrencyUnit moeda;
	private final AtomicBoolean encerrado;

	public Leilao(LeilaoBuilder builder) {
		this.id 		= builder.getId();
		this.descricao 	= builder.getDescricao();
		this.moeda 		= builder.getMoeda();
		this.data 		= builder.getDataInicio();
		this.encerrado 	= builder.getEncerrado();
		this.lances = new ArrayList<>();
		builder.getLances().stream().forEach(lance -> this.propor(lance));
	}
	
	
	
	public static Leilao leilao(int id, String descricaoItem, LocalDate dataInicio, CurrencyUnit moedaParaLance) {
		return fromBuilder()
			.descricao(descricaoItem)
			.dataInicio(dataInicio)
			.moedaParaLance(moedaParaLance)
		.create();
	}
	
	public static Leilao leilao(String descricaoItem, LocalDate dataInicio, CurrencyUnit moedaParaLance) {
		return fromBuilder()
			.descricao(descricaoItem)
			.dataInicio(dataInicio)
			.moedaParaLance(moedaParaLance)
		.create();
	}
	
	public static LeilaoBuilder fromBuilder() {
		return new LeilaoBuilder();
	}
	
	public static Leilao fromResultSet(ResultSet rs, CurrencyUnit real) throws SQLException {
		return fromBuilder()
				.id(rs.getInt("id"))
				.descricao(rs.getString("descricao"))
				.dataInicio(rs.getDate("data").toLocalDate())
				.moedaParaLance(real)
				.encerrado(rs.getBoolean("encerrado"))
				.create();
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public CurrencyUnit getMoeda() {
		return moeda;
	}

	public boolean isEncerrado() {
		return encerrado.get();
	}

	public void encerrar() {
		this.encerrado.set(true);
	}

	public void propor(Lance lance) {
		if (!podeDar(lance)) {
			return;
		}

		lances.add(lance);
	}

	private boolean podeDar(Lance lance) {

		if (lances.isEmpty())
			return true;

		if (ultimoLanceDado().filter(l -> l.getUsuario().equals(lance.getUsuario())).isPresent())
			return false;

		if (ultimoLanceDado().filter(l -> l.getValor().isGreaterThanOrEqualTo(lance.getValor())).isPresent())
			return false;

		if (quantidadeLancesDo(lance.getUsuario()) >= 5)
			return false;

		return true;
	}

	public void dobrarLance(Usuario usuario) {
		Optional<Lance> ultimoLance = ultimoLanceDo(usuario);

		if (!ultimoLance.isPresent())
			return;

		propor(lance(usuario, ultimoLance.get().getValor().multiply(2)));
	}

	private long quantidadeLancesDo(Usuario usuario) {
		long quantidadeLance = lances.stream().map(Lance::getUsuario).filter(u -> u.equals(usuario)).count();
		return quantidadeLance;
	}

	private Optional<Lance> ultimoLanceDado() {
		Optional<Lance> ultimo = lances.stream().reduce((a, b) -> b);
		return ultimo;
	}

	private Optional<Lance> ultimoLanceDo(Usuario usuario) {
		Optional<Lance> ultimo = lances.stream().filter(lance -> lance.getUsuario().equals(usuario))
				.reduce((a, b) -> b);
		return ultimo;
	}
}