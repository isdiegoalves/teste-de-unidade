package br.diego.leilaoonline.leilao.model;

import static br.diego.leilaoonline.lance.model.Lance.lance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.usuario.model.Usuario;

@Entity
public class Leilao {

	@Id @GeneratedValue
	private final Long id;
	private final LocalDate dataAbertura;
	private final String descricao;
	private final MonetaryAmount valorInicial;
	private final CurrencyUnit moeda;
	@ManyToOne
	private final Usuario dono;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="leilao")
	private final List<Lance> lances;
	private final AtomicBoolean encerrado;
	private final boolean usado;

	public Leilao(LeilaoBuilder builder) {
		this.id 			= builder.getId();
		this.descricao 		= builder.getDescricao();
		this.moeda 			= builder.getMoeda();
		this.dataAbertura 	= builder.getDataAbertura();
		this.encerrado 		= builder.getEncerrado();
		this.valorInicial 	= builder.getValorInicial();
		this.usado 			= builder.isUsado();
		this.dono 			= builder.getDono();
		this.lances 		= new ArrayList<>();
		builder.getLances().stream().forEach(lance -> this.propor(lance));
	}
	
	public static Leilao leilao(int id, String descricaoItem, LocalDate dataAbertura, CurrencyUnit moedaParaLance) {
		return builder()
			.descricao(descricaoItem)
			.dataAbertura(dataAbertura)
			.moedaParaLance(moedaParaLance)
		.create();
	}
	
	public static Leilao leilao(String descricaoItem, LocalDate dataAbertura, CurrencyUnit moedaParaLance) {
		return builder()
			.descricao(descricaoItem)
			.dataAbertura(dataAbertura)
			.moedaParaLance(moedaParaLance)
		.create();
	}
	
	public static LeilaoBuilder builder() {
		return new LeilaoBuilder();
	}
	
	public static Leilao fromResultSet(ResultSet rs, CurrencyUnit real) throws SQLException {
		return builder()
				.id(rs.getLong("id"))
				.descricao(rs.getString("descricao"))
				.dataAbertura(rs.getDate("data").toLocalDate())
				.moedaParaLance(real)
				.encerrado(rs.getBoolean("encerrado"))
				.create();
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

		propor(lance(usuario, ultimoLance.get().getValor().multiply(2), this));
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

	public MonetaryAmount getValorInicial() {
		return valorInicial;
	}

	public Usuario getDono() {
		return dono;
	}

	public boolean isUsado() {
		return usado;
	}
}