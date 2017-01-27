package br.diego.leilaoonline.leilao.repository;

import java.time.LocalDate;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public interface LeilaoRepository {

	MonetaryAmount getValorInicialMedioDoUsuario(Usuario usuario, CurrencyUnit moeda);
	List<Leilao> listaLeiloesDoUsuario(Usuario usuario);
	void deletaEncerrados();
	void deleta(Leilao leilao);
	void atualiza(Leilao leilao);
	Long total();
	List<Leilao> disputadosEntre(MonetaryAmount inicio, MonetaryAmount fim);
	List<Leilao> porPeriodo(LocalDate inicio, LocalDate fim);
	List<Leilao> antigos();
	List<Leilao> novos();
	List<Leilao> encerrados();
	Leilao porId(Long id);
	Long salvar(Leilao leilao);
}
