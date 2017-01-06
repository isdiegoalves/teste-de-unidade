package br.com.caelum.leilao.dominio;

import static br.com.caelum.leilao.dominio.Lance.lance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.money.CurrencyUnit;

public class Leilao {

	private String descricao;
	private List<Lance> lances;
	private CurrencyUnit moeda;

	public Leilao(String descricao, CurrencyUnit moeda) {
		this.descricao = descricao;
		this.lances = new ArrayList<Lance>();
		this.moeda = moeda;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public CurrencyUnit getMoeda() {
		return moeda;
	}

	public void propoe(Lance lance) {
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
		
		propoe(lance(usuario, ultimoLance.get().getValor().multiply(2)));
	}

	private long quantidadeLancesDo(Usuario usuario) {
		long quantidadeLance = lances.stream()
				.map(Lance::getUsuario)
				.filter(u -> u.equals(usuario))
				.count();
		return quantidadeLance;
	}

	private Optional<Lance> ultimoLanceDado() {
		Optional<Lance> ultimo = lances.stream()
				.reduce((a, b) -> b);
		return ultimo;
	}

	private Optional<Lance> ultimoLanceDo(Usuario usuario) {
		Optional<Lance> ultimo = lances.stream()
				.filter(lance -> lance.getUsuario().equals(usuario))
				.reduce((a, b) -> b);
		return ultimo;
	}
}