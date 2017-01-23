package br.diego.leilaoonline.avaliador.entity;

import static org.javamoney.moneta.function.MonetaryOperators.rounding;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.MonetaryFunctions;
import org.javamoney.moneta.function.MonetarySummaryStatistics;

import br.diego.leilaoonline.lance.entity.Lance;
import br.diego.leilaoonline.leilao.entity.Leilao;

public class Avaliador {

	private MonetarySummaryStatistics leilaoSumarizado;
	private List<Lance> maioresLances = Collections.emptyList();
	
	public void avalia(Leilao leilao) {
		
		if (leilao.getLances().isEmpty()) {
			throw new UnsupportedOperationException("Não é possível avaliar um leilão sem lances!");
		}
		
		leilaoSumarizado = leilao.getLances().stream()
				.map(lance -> lance.getValor())
				.filter(Objects::nonNull)
				.collect(MonetaryFunctions.groupBySummarizingMonetary()).get()
				.get(leilao.getMoeda());

		maioresLances = leilao.getLances().stream()
				.filter(Objects::nonNull)
				.sorted((s1, s2) -> s2.getValor().compareTo(s1.getValor()))
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
		return leilaoSumarizado.getAverage().with(rounding(2));
	}
	
	public List<Lance> getMaioresLances() {
		return maioresLances;
	}
}
