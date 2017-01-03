package br.com.caelum.leilao.dominio;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;

public class Avaliador {

	private MonetaryAmount maiorDeTodos;
	private MonetaryAmount menorDeTodos;
	
	public void avalia(Leilao leilao) {

		maiorDeTodos = leilao.getLances().stream()
				.map(lance -> lance.getValor())
				.reduce(MonetaryFunctions.max())
				.orElse(null);

		menorDeTodos = leilao.getLances().stream()
				.map(lance -> lance.getValor())
				.reduce(MonetaryFunctions.min())
				.orElse(null);
	}
	
	public MonetaryAmount getMaiorDeTodos() {
		return maiorDeTodos;
	}
	
	public MonetaryAmount getMenorDeTodos() {
		return menorDeTodos;
	}
}
