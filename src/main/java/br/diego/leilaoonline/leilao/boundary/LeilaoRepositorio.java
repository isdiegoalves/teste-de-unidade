package br.diego.leilaoonline.leilao.boundary;

import java.util.List;

import br.diego.leilaoonline.leilao.entity.Leilao;

public interface LeilaoRepositorio {
	void salva(Leilao leilao);
    List<Leilao> encerrados();
    List<Leilao> correntes();
    void atualiza(Leilao leilao);
}
