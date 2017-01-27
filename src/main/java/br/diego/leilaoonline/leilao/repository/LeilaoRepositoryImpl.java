package br.diego.leilaoonline.leilao.repository;

import java.time.LocalDate;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.hibernate.Session;
import org.javamoney.moneta.RoundedMoney;
import org.javamoney.moneta.function.MonetaryOperators;

import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LeilaoRepositoryImpl {

	private final Session session;

	public LeilaoRepositoryImpl(Session session) {
		this.session = session;
	}

	public Long salvar(Leilao leilao) {
		Long idLeilao = (Long) session.save(leilao);

		for(Lance lance : leilao.getLances()) {
			session.save(lance);
		}
		
		return idLeilao;
	}

	public Leilao porId(Long id) {
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
				.setParameter("encerrado", false)
				.getResultList();
	}

	public List<Leilao> disputadosEntre(MonetaryAmount inicio, MonetaryAmount fim) {
		return session.createQuery("from Leilao l where l.valorInicial " +
				"between :inicio and :fim and l.encerrado = :encerrado " +
				"and size(l.lances) > 1", Leilao.class)
				.setParameter("inicio", inicio)
				.setParameter("fim", fim)
				.setParameter("encerrado", false)
				.getResultList();
	}

	public Long total() {
		return (Long) session.createQuery("select count(l) from Leilao l where l.encerrado = :encerrado")
				.setParameter("encerrado", false)
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

	public MonetaryAmount getValorInicialMedioDoUsuario(Usuario usuario, CurrencyUnit moeda) {
		Double media = (Double) session
				.createQuery(new StringBuilder()
						.append(" select avg(leilao.valorInicial) ")
						.append("	from Leilao leilao")
						.append("	left join leilao.lances lance")
						.append(" where lance.usuario = :usuario ")
						.append(" and leilao.moeda = :moeda")
						.toString())
				.setParameter("usuario", usuario)
				.setParameter("moeda", moeda)
				.getSingleResult();

		return RoundedMoney.of(media, moeda, MonetaryOperators.rounding());
	}
}
