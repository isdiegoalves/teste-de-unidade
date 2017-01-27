package br.diego.leilaoonline.pagamento.service;

import static br.diego.leilaoonline.pagamento.model.Pagamento.pagamento;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.LocalDate;

import br.diego.leilaoonline.avaliador.model.Avaliador;
import br.diego.leilaoonline.infra.relogio.Relogio;
import br.diego.leilaoonline.infra.relogio.internal.RelogioDoSistema;
import br.diego.leilaoonline.leilao.repository.LeilaoRepository;
import br.diego.leilaoonline.pagamento.model.Pagamento;
import br.diego.leilaoonline.pagamento.repository.PagamentoRepository;

public class GeradorPagamentoServiceImpl implements GeradorPagamentoService {

	private final Avaliador avaliador;
    private final PagamentoRepository pagamentos;
    private final LeilaoRepository leiloes;
	private final Relogio relogio;
    
    public GeradorPagamentoServiceImpl(LeilaoRepository leiloes, PagamentoRepository pagamentos, Avaliador avaliador, Relogio relogio) {
        this.leiloes = leiloes;
        this.pagamentos = pagamentos;
        this.avaliador = avaliador;
        this.relogio = relogio;
    }

	public GeradorPagamentoServiceImpl(LeilaoRepository leiloes, PagamentoRepository pagamentos, Avaliador avaliador) {
        this(leiloes, pagamentos,  avaliador, new RelogioDoSistema());
    }

    @Override
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