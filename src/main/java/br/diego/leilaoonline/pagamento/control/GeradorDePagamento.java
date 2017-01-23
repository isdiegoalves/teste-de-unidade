package br.diego.leilaoonline.pagamento.control;

import static br.diego.leilaoonline.pagamento.entity.Pagamento.pagamento;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.LocalDate;

import br.diego.leilaoonline.avaliador.entity.Avaliador;
import br.diego.leilaoonline.infra.relogio.boundary.Relogio;
import br.diego.leilaoonline.infra.relogio.control.RelogioDoSistema;
import br.diego.leilaoonline.leilao.boundary.LeilaoRepositorio;
import br.diego.leilaoonline.pagamento.entity.Pagamento;

public class GeradorDePagamento {

	private final Avaliador avaliador;
    private final PagamentoRepositorio pagamentos;
    private final LeilaoRepositorio leiloes;
	private final Relogio relogio;
    
    public GeradorDePagamento(LeilaoRepositorio leiloes, PagamentoRepositorio pagamentos, Avaliador avaliador, Relogio relogio) {
        this.leiloes = leiloes;
        this.pagamentos = pagamentos;
        this.avaliador = avaliador;
        this.relogio = relogio;
    }

	public GeradorDePagamento(LeilaoRepositorio leiloes, PagamentoRepositorio pagamentos, Avaliador avaliador) {
        this(leiloes, pagamentos,  avaliador, new RelogioDoSistema());
    }

    public void gerar() {
    	leiloes.encerrados().stream()
    	.forEach(leilao -> {
    		avaliador.avalia(leilao);
            Pagamento novoPagamento = pagamento(avaliador.getMaiorLance(), primeiroDiaUtil());
            pagamentos.salvar(novoPagamento);
    	});
    }
    
	private LocalDate primeiroDiaUtil() {
		LocalDate diaUtil = relogio.hoje();
		
		do {
			diaUtil = diaUtil.plusDays(1);
		} while (diaUtil.getDayOfWeek() == SATURDAY || diaUtil.getDayOfWeek() == SUNDAY);

		return diaUtil;
	}
}