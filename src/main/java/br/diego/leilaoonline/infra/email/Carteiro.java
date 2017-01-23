package br.diego.leilaoonline.infra.email;

import br.diego.leilaoonline.leilao.entity.Leilao;

public interface Carteiro {

	void enviar(Leilao leilao);
}
