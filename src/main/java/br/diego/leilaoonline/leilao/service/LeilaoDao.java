package br.diego.leilaoonline.leilao.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.money.MonetaryAmount;

import org.hibernate.Session;

import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LeilaoDao {

	private final Session session;

	public LeilaoDao(Session session) {
		this.session = session;
	}

	public void salvar(Leilao leilao) {
		session.save(leilao);

		for(Lance lance : leilao.getLances()) {
			session.save(lance);
		}
	}

	public Leilao porId(int id) {
		return (Leilao) session.get(Leilao.class, id);
	}

	public List<Leilao> novos() {
		return session.createQuery("from Leilao l where usado = false", Leilao.class)
				.getResultList();
	}

	public List<Leilao> antigos() {
		LocalDate seteDiasAtras = LocalDate.now().minusDays(7);

		return session.createQuery("from Leilao l where dataAbertura < :data", Leilao.class)
				.setParameter("data", seteDiasAtras)
				.getResultList();
	}

	public List<Leilao> porPeriodo(LocalDate inicio, LocalDate fim) {
		return session.createQuery("from Leilao l where l.dataAbertura "
				+ "between :inicio and :fim and l.encerrado = :encerrado", Leilao.class)
				.setParameter("inicio", inicio)
				.setParameter("fim", fim)
				.setParameter("encerrado", new AtomicBoolean(false))
				.getResultList();
	}

	public List<Leilao> disputadosEntre(MonetaryAmount inicio, MonetaryAmount fim) {
		return session.createQuery("from Leilao l where l.valorInicial " +
				"between :inicio and :fim and l.encerrado = :encerrado " +
				"and size(l.lances) > 1", Leilao.class)
				.setParameter("inicio", inicio)
				.setParameter("fim", fim)
				.setParameter("encerrado", new AtomicBoolean(false))
				.getResultList();
	}

	public Long total() {
		return (Long) session.createQuery("select count(l) from Leilao l where l.encerrado = :encerrado")
				.setParameter("encerrado", new AtomicBoolean(false))
				.getSingleResult();
	}

	public void atualiza(Leilao leilao) {
		session.merge(leilao);
	}

	public void deleta(Leilao leilao) {
		session.delete(leilao);
	}

	public void deletaEncerrados() {
		session
		.createQuery("delete from Leilao l where l.encerrado = true")
		.executeUpdate();
	}

	public List<Leilao> listaLeiloesDoUsuario(Usuario usuario) {
		return session.createQuery("select distinct lance.leilao " +
				"from Lance lance " +
				"where lance.usuario = :usuario", Leilao.class)
				.setParameter("usuario", usuario).getResultList();
	}

	public double getValorInicialMedioDoUsuario(Usuario usuario) {
		return (Double) session.createQuery("select avg(lance.leilao.valorInicial) " +
				"from Lance lance " +
				"where lance.usuario = :usuario")
				.setParameter("usuario", usuario)
				.getSingleResult();
	}
}
