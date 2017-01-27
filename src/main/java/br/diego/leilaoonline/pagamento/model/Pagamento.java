package br.diego.leilaoonline.pagamento.model;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

public class Pagamento {

	private MonetaryAmount valor;
	private LocalDate data;

	private Pagamento(MonetaryAmount valor, LocalDate data) {
		this.valor = valor;
		this.data = data;
	}

	public static Pagamento pagamento(MonetaryAmount valor, LocalDate data) {
		return new Pagamento(valor, data);
	}

	public MonetaryAmount getValor() {
		return valor;
	}

	public LocalDate getData() {
		return data;
	}
}
