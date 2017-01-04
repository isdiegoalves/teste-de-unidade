package br.com.caelum.leilao.dominio;

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
		if (!podeDarLance(lance.getUsuario())) {
			return;
		}

		lances.add(lance);
	}

	private boolean podeDarLance(Usuario usuario) {

		if (lances.isEmpty())
			return true;

		if (ultimoLanceDado().getUsuario().equals(usuario))
			return false;

		if (quantidadeLancesUsuario(usuario) >= 5)
			return false;

		return true;
	}

	private long quantidadeLancesUsuario(Usuario usuario) {
		long quantidadeLance = lances.stream()
				.map(Lance::getUsuario)
				.filter(u -> u.equals(usuario))
				.count();
		return quantidadeLance;
	}

	private Lance ultimoLanceDado() {
		return lances.get(lances.size()-1);
	}

	public void dobrarLance(Usuario usuario) {
		Optional<Lance> ultimo = ultimoLanceDo(usuario);

		if (!ultimo.isPresent())
			return;

		Lance ultimoLance = ultimo.get();
		propoe(new Lance(usuario, ultimoLance.getValor().multiply(2)));
	}

	private Optional<Lance> ultimoLanceDo(Usuario usuario) {
		Optional<Lance> ultimo = lances.stream()
				.filter(lance -> lance.getUsuario().equals(usuario))
				.reduce((a, b) -> b);
		return ultimo;
	}
}