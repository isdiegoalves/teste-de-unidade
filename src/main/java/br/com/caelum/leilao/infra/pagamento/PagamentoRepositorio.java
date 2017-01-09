package br.com.caelum.leilao.infra.pagamento;

import br.com.caelum.leilao.dominio.Pagamento;

public interface PagamentoRepositorio {

	void salvar(Pagamento pagamento);
}
