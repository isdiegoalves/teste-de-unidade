package br.com.caelum.leilao.servico;

import static br.com.caelum.leilao.dominio.Pagamento.pagamento;
import static java.time.LocalDate.now;

import br.com.caelum.leilao.dominio.Avaliador;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.infra.dao.LeilaoRepositorio;
import br.com.caelum.leilao.infra.pagamento.PagamentoRepositorio;

public class GeradorDePagamento {

	private final Avaliador avaliador;
    private final PagamentoRepositorio pagamentos;
    private final LeilaoRepositorio leiloes;

	public GeradorDePagamento(LeilaoRepositorio leiloes, PagamentoRepositorio pagamentos, Avaliador avaliador) {
        this.leiloes = leiloes;
        this.pagamentos = pagamentos;
        this.avaliador = avaliador;
    }

    public void gerar() {
    	leiloes.encerrados().stream()
    	.forEach(leilao -> {
    		avaliador.avalia(leilao);
            Pagamento novoPagamento = pagamento(avaliador.getMaiorLance(), now());
            pagamentos.salvar(novoPagamento);
    	});
    }
}