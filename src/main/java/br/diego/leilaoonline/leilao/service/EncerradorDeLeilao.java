package br.diego.leilaoonline.leilao.service;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;

import br.diego.leilaoonline.infra.email.Carteiro;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.leilao.repository.LeilaoRepositorio;

public class EncerradorDeLeilao {

	private int total = 0;
	private final LeilaoRepositorio repositorio;
	private final Carteiro carteiro;

	public EncerradorDeLeilao(LeilaoRepositorio repositorio, Carteiro carteiro) {
		this.repositorio = repositorio;
		this.carteiro = carteiro;
	}

	public void encerra() {
		repositorio.correntes().stream()
		.filter(leilao -> comecouSemanaPassada(leilao))
		.forEach(leilao -> {
			try {
				leilao.encerrar();
				total++;
				repositorio.atualiza(leilao);
				carteiro.enviar(leilao);
			} catch (Exception e) {
				//salva a excecao no sistema de logs e continua
			}
		});
	}

	private boolean comecouSemanaPassada(Leilao leilao) {
		return diasEntre(leilao.getDataAbertura(), now()) >= 7;
	}

	private long diasEntre(LocalDate inicio, LocalDate fim) {
		return DAYS.between(inicio, fim);
	}

	public int getTotalEncerrados() {
		return total;
	}
}
