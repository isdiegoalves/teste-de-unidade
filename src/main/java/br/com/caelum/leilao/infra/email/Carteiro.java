package br.com.caelum.leilao.infra.email;

import br.com.caelum.leilao.dominio.Leilao;

public interface Carteiro {

	void enviar(Leilao leilao);
}
