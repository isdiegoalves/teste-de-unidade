package br.diego.leilaoonline.leilao.control;

import java.util.ArrayList;
import java.util.List;

import br.diego.leilaoonline.leilao.boundary.LeilaoRepositorio;
import br.diego.leilaoonline.leilao.entity.Leilao;

public class LeilaoDao2 implements LeilaoRepositorio {

	private static List<Leilao> leiloes = new ArrayList<Leilao>();;
	
	public void salva(Leilao leilao) {
		leiloes.add(leilao);
	}

	public List<Leilao> encerrados() {
		
		List<Leilao> filtrados = new ArrayList<Leilao>();
		for(Leilao leilao : leiloes) {
			if(leilao.isEncerrado()) filtrados.add(leilao);
		}

		return filtrados;
	}
	
	public List<Leilao> correntes() {
		
		List<Leilao> filtrados = new ArrayList<Leilao>();
		for(Leilao leilao : leiloes) {
			if(!leilao.isEncerrado()) filtrados.add(leilao);
		}

		return filtrados;
	}
	
	public void atualiza(Leilao leilao) { /* faz nada! */ }
	
	public String teste() {
		return "teste";
	}
}
