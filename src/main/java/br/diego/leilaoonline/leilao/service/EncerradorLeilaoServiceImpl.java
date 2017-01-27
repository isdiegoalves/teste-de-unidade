package br.diego.leilaoonline.leilao.service;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;

import br.diego.leilaoonline.infra.email.Carteiro;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.leilao.repository.LeilaoRepository;

public class EncerradorLeilaoServiceImpl implements EncerradorLeilaoService {

	private int total = 0;
	private final LeilaoRepository repositorio;
	private final Carteiro carteiro;

	public EncerradorLeilaoServiceImpl(LeilaoRepository repositorio, Carteiro carteiro) {
		this.repositorio = repositorio;
		this.carteiro = carteiro;
	}

	@Override
	public void encerra() {
		repositorio.novos().stream()
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

	@Override
	public int totalEncerrados() {
		return total;
	}
}
