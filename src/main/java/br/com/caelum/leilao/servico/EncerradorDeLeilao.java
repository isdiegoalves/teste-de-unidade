package br.com.caelum.leilao.servico;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;

import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoRepositorio;
import br.com.caelum.leilao.infra.email.Carteiro;

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
		return diasEntre(leilao.getData(), now()) >= 7;
	}

	private long diasEntre(LocalDate inicio, LocalDate fim) {
		return DAYS.between(inicio, fim);
	}

	public int getTotalEncerrados() {
		return total;
	}
}
