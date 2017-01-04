package br.com.caelum.leilao.dominio;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;
import org.javamoney.moneta.function.MonetaryOperators;
import org.javamoney.moneta.function.MonetarySummaryStatistics;

public class Avaliador {

	private MonetarySummaryStatistics leilaoSumarizado;
	private List<MonetaryAmount> maioresValores = Collections.emptyList();
	
	public void avalia(Leilao leilao) {
		
		leilaoSumarizado = leilao.getLances().stream()
				.map(lance -> lance.getValor())
				.filter(Objects::nonNull)
				.collect(MonetaryFunctions.groupBySummarizingMonetary()).get()
				.get(leilao.getMoeda());

		maioresValores = leilao.getLances().stream()
				.map(lance -> lance.getValor())
				.filter(Objects::nonNull)
				.distinct()
				.sorted((s1, s2) -> s2.compareTo(s1))
				.limit(3)
				.collect(Collectors.toList());
	}
	
	public MonetaryAmount getMaiorLance() {
		return leilaoSumarizado.getMax();
	}
	
	public MonetaryAmount getMenorLance() {
		return leilaoSumarizado.getMin();
	}

	public MonetaryAmount getMediaLances() {
		return leilaoSumarizado.getAverage()
				.with(MonetaryOperators.rounding(2));
	}
	
	public List<MonetaryAmount> getMaioresValores() {
		return maioresValores;
	}
}
