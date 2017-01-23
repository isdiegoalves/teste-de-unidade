package br.diego.leilaoonline.leilao.repository;

import java.util.List;

import br.diego.leilaoonline.leilao.model.Leilao;

public interface LeilaoRepositorio {
	void salva(Leilao leilao);
    List<Leilao> encerrados();
    List<Leilao> correntes();
    void atualiza(Leilao leilao);
}
