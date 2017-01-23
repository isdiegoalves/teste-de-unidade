package br.diego.leilaoonline.infra.email;

import br.diego.leilaoonline.leilao.model.Leilao;

public interface Carteiro {

	void enviar(Leilao leilao);
}
