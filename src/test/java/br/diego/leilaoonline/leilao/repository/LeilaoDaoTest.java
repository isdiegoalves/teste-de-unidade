package br.diego.leilaoonline.leilao.repository;

import static br.diego.leilaoonline.lance.model.Lance.lance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.nullValue;
import static org.javamoney.moneta.function.MonetaryOperators.rounding;

import java.time.LocalDate;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.hibernate.Session;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.RoundedMoney;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.diego.leilaoonline.infra.persistence.hibernate.CriadorDeSessao;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;
import br.diego.leilaoonline.usuario.repository.UsuarioDao;

public class LeilaoDaoTest {

	private Session session;
	private LeilaoDao leilaoDao;
	private CurrencyUnit dollar;
	private CurrencyUnit real;
	private UsuarioDao usuarioDao;
	
	@Before
	public void before() {
		real = Monetary.getCurrency("BRL");
		dollar = Monetary.getCurrency("USD");
		session = new CriadorDeSessao().session();
		session.beginTransaction();
		leilaoDao = new LeilaoDao(session);
		usuarioDao = new UsuarioDao(session);
	}
	
	@After
	public void after() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveTrazerSomenteLeiloesNovos() {
		
		Leilao leilaoAntigo = Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.of(2017, 1, 10))
		.encerrado(false)
		.valorInicial(Money.of(1000, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(Usuario.fromId(1L))
		.create();
		
		Leilao leilaoMuitoAntigo = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(LocalDate.of(2017, 1, 1))
				.encerrado(false)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(Usuario.fromId(1L))
				.create();
		
		Leilao leilaoNovo = Leilao.builder()
				.descricao("Televisão")
				.dataAbertura(LocalDate.of(2017, 1, 1))
				.encerrado(false)
				.usado(true)
				.dono(Usuario.fromId(1L))
				.create();
		
		leilaoDao.salvar(leilaoAntigo);
		leilaoDao.salvar(leilaoMuitoAntigo);
		leilaoDao.salvar(leilaoNovo);
		
		List<Leilao> novos = leilaoDao.novos();
		
		assertThat(novos, hasSize(2));
		assertThat(novos.get(0).getDescricao(), equalTo("Geladeira"));
		assertThat(novos.get(1).getDescricao(), equalTo("Playstation"));
	}
	
	@Test
	public void naoDeveEncontrarLeiloesNovos() {
		
		List<Leilao> novos = leilaoDao.novos();
		
		assertThat(novos, empty());
	}
	
	@Test
	public void deveContarTotalLeiloesNaoEncerrados() {
		
		Leilao leilao = Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.now())
		.encerrado(false)
		.usado(false)
		.dono(Usuario.fromId(1L))
		.create();
		
		Leilao leilaoAIgnorar = Leilao.builder()
				.descricao("Geladeira")
				.dataAbertura(LocalDate.now())
				.encerrado(true)
				.usado(false)
				.dono(Usuario.fromId(1L))
				.create();
		
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilaoAIgnorar);
		
		Long total = leilaoDao.total();
		
		assertThat(total, equalTo(1L));
	}
	
	@Test
	public void naoDeveTrazerLeiloesEncerrados() {
		
		Leilao leilao = Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.now())
		.encerrado(true)
		.usado(true)
		.dono(Usuario.fromId(1L))
		.create();
		
		leilaoDao.salvar(leilao);
		
		Long total = leilaoDao.total();
		
		assertThat(total, equalTo(0L));
	}
	
	@Test
	public void deveTrazerLeilaoCriadoExatamenteA7DiasPorPeriodo() {
		
		LocalDate seteDiasAtras = LocalDate.now().minusDays(7);
		Leilao primeiroLeilaoAbertoA7Dias= Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(seteDiasAtras)
		.encerrado(false)
		.valorInicial(Money.of(1000, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(Usuario.fromId(1L))
		.create();
		
		Leilao segundoLeilaoAbertoA7Dias = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(seteDiasAtras)
				.encerrado(false)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(Usuario.fromId(1L))
				.create();
		
		leilaoDao.salvar(primeiroLeilaoAbertoA7Dias);
		leilaoDao.salvar(segundoLeilaoAbertoA7Dias);
		
		List<Leilao> novos = leilaoDao.porPeriodo(seteDiasAtras, seteDiasAtras);
		
		assertThat(novos, hasSize(2));
		assertThat(novos.get(0).getDescricao(), equalTo("Geladeira"));
		assertThat(novos.get(1).getDescricao(), equalTo("Playstation"));
	}
	
	@Test
	public void naoDeveTrazerLeiloesEncerradosPorPeriodo() {
		
		LocalDate seteDiasAtras = LocalDate.now().minusDays(7);
		Leilao leilaoAbertoA6Dias= Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(seteDiasAtras)
		.encerrado(true)
		.valorInicial(Money.of(1000, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(Usuario.fromId(1L))
		.create();
		
		Leilao leilaoAbertoA7Dias = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(seteDiasAtras)
				.encerrado(true)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(Usuario.fromId(1L))
				.create();
		
		leilaoDao.salvar(leilaoAbertoA6Dias);
		leilaoDao.salvar(leilaoAbertoA7Dias);
		
		List<Leilao> novos = leilaoDao.porPeriodo(seteDiasAtras, seteDiasAtras);
		
		assertThat(novos, hasSize(0));
	}
	
	@Test
	public void naoDeveTrazerLeiloeInexistentesPorPeriodo() {
		
		LocalDate seteDiasAtras = LocalDate.now().minusDays(7);
		Leilao leilaoAberto= Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.now())
		.encerrado(false)
		.valorInicial(Money.of(1000, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(Usuario.fromId(1L))
		.create();
		
		Leilao leilaoAbertoA7Dias = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(seteDiasAtras)
				.encerrado(true)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(Usuario.fromId(1L))
				.create();
		
		leilaoDao.salvar(leilaoAberto);
		leilaoDao.salvar(leilaoAbertoA7Dias);
		
		List<Leilao> novos = leilaoDao.porPeriodo(seteDiasAtras, seteDiasAtras);
		assertThat(novos, hasSize(0));
	}
	
	@Test
	public void deveEncontrarSomenteLeiloesAntigosAbertosAMaisDe7Dias() {
		
		Leilao leilaoAbertoA6Dias= Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.now().minusDays(6))
		.encerrado(false)
		.valorInicial(Money.of(1000, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(Usuario.fromId(1L))
		.create();
		
		Leilao leilaoAbertoA7Dias = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(LocalDate.now().minusDays(7))
				.encerrado(false)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(Usuario.fromId(1L))
				.create();
		
		Leilao leilaoAbertoA8Dias = Leilao.builder()
				.descricao("Televisão")
				.dataAbertura(LocalDate.now().minusDays(8))
				.encerrado(false)
				.usado(true)
				.dono(Usuario.fromId(1L))
				.create();
		
		leilaoDao.salvar(leilaoAbertoA6Dias);
		leilaoDao.salvar(leilaoAbertoA7Dias);
		leilaoDao.salvar(leilaoAbertoA8Dias);
		
		List<Leilao> novos = leilaoDao.antigos();
		
		assertThat(novos, hasSize(1));
		assertThat(novos.get(0).getDescricao(), equalTo("Televisão"));
	}
	
	@Test
	public void deveTrazerLeiloesDisputadosEntreComMaisDeTresLances() {
		
		Usuario diego = usuarioDao.porId(1L);
		Usuario isabela = usuarioDao.porId(2L);
		
		Leilao leilao1= Leilao.builder()
		.descricao("Geladeira")
		.encerrado(false)
		.valorInicial(Money.of(500, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(diego)
		.create();
		leilao1.propor(lance(diego,  	Money.of(1000, real), leilao1));
		leilao1.propor(lance(isabela,	Money.of(1100, real), leilao1));
		leilao1.propor(lance(diego,  	Money.of(1200, real), leilao1));
		leilao1.propor(lance(isabela,	Money.of(1300, real), leilao1));
		leilao1.propor(lance(diego,  	Money.of(1500, real), leilao1));
		leilao1.propor(lance(isabela,	Money.of(1700, real), leilao1));
		leilao1.propor(lance(diego,  	Money.of(1800, real), leilao1));
		leilao1.propor(lance(isabela,	Money.of(2000, real), leilao1));
		
		Leilao leilao2 = Leilao.builder()
				.descricao("Playstation")
				.encerrado(false)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(isabela)
				.create();
		
		leilao2.propor(lance(diego,  	Money.of(1000, real), leilao2));
		leilao2.propor(lance(isabela,	Money.of(1100, real), leilao2));
		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);
		
		List<Leilao> novos = leilaoDao.disputadosEntre(Money.zero(real), Money.of(3000, real));
		
		assertThat(novos, hasSize(2));
		assertThat(novos.get(0).getDescricao(), equalTo("Geladeira"));
	}
	
	@Test
	public void deveTrazerLeiloesDoUsuario() {
		
		Usuario diego = usuarioDao.porId(1L);
		Usuario isabela = usuarioDao.porId(2L);
		
		Leilao leilao1= Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.now())
		.encerrado(false)
		.valorInicial(Money.of(500, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(diego)
		.create();
		
		leilao1.propor(lance(diego,  	Money.of(1000, real), leilao1));
		leilao1.propor(lance(isabela,	Money.of(1100, real), leilao1));
		leilao1.propor(lance(diego,  	Money.of(1200, real), leilao1));
		leilao1.propor(lance(isabela,	Money.of(1300, real), leilao1));

		
		Leilao leilao2 = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(LocalDate.now())
				.encerrado(false)
				.moedaParaLance(dollar)
				.usado(false)
				.dono(isabela)
				.create();
		
		leilao2.propor(lance(diego,  	Money.of(1000, real), leilao2));
		leilao2.propor(lance(isabela,	Money.of(1100, real), leilao2));
		
		
		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);
		
		List<Leilao> novos = leilaoDao.listaLeiloesDoUsuario(diego);
		
		assertThat(novos, hasSize(2));
		assertThat(novos.get(0).getDescricao(), equalTo("Geladeira"));
		assertThat(novos.get(1).getDescricao(), equalTo("Playstation"));
	}
	
	@Test
	public void deveTrazerMediaLanceInicialLeiloes() {
		
		Usuario diego = usuarioDao.porId(1L);
		Usuario isabela = usuarioDao.porId(2L);
		
		Leilao leilaoTV = Leilao.builder()
				.descricao("Televisão")
				.dataAbertura(LocalDate.now())
				.encerrado(false)
				.valorInicial(Money.of(200, real))
				.moedaParaLance(real)
				.usado(false)
				.dono(diego)
				.create();
		
		leilaoTV.propor(lance(diego,  	Money.of(200, real), leilaoTV));
		leilaoTV.propor(lance(isabela,	Money.of(300, real), leilaoTV));
		leilaoTV.propor(lance(diego,  	Money.of(350, real), leilaoTV));
		
		Leilao leilaoGeladeira= Leilao.builder()
		.descricao("Geladeira")
		.dataAbertura(LocalDate.now())
		.encerrado(false)
		.valorInicial(Money.of(500, real))
		.moedaParaLance(real)
		.usado(false)
		.dono(diego)
		.create();
		
		leilaoGeladeira.propor(lance(diego,  	Money.of(1000, real), leilaoGeladeira));
		leilaoGeladeira.propor(lance(isabela,	Money.of(1100, real), leilaoGeladeira));
		leilaoGeladeira.propor(lance(diego,  	Money.of(1200, real), leilaoGeladeira));
		leilaoGeladeira.propor(lance(isabela,	Money.of(1300, real), leilaoGeladeira));

		
		Leilao leilaoPlaystation = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(LocalDate.now())
				.encerrado(false)
				.valorInicial(Money.of(800, real))
				.moedaParaLance(dollar)
				.usado(false)
				.dono(isabela)
				.create();
		
		leilaoPlaystation.propor(lance(diego,  	Money.of(1000, real), leilaoPlaystation));
		leilaoPlaystation.propor(lance(isabela,	Money.of(1100, real), leilaoPlaystation));
		
		leilaoDao.salvar(leilaoTV);
		leilaoDao.salvar(leilaoGeladeira);
		leilaoDao.salvar(leilaoPlaystation);
		
		MonetaryAmount media = leilaoDao.getValorInicialMedioDoUsuario(diego, real);
		
		assertThat(media, equalTo(RoundedMoney.of(350, real, rounding())));
	}
	
	
	@Test
	public void deveDeletarUmLeilao() {
		
		Usuario diego = usuarioDao.porId(1L);
		
		Leilao leilaoPlaystation = Leilao.builder()
				.descricao("Playstation")
				.dataAbertura(LocalDate.now())
				.encerrado(false)
				.valorInicial(Money.of(800, real))
				.moedaParaLance(dollar)
				.usado(false)
				.dono(diego)
				.create();
		
		leilaoDao.salvar(leilaoPlaystation);
		leilaoDao.deleta(leilaoPlaystation);
		Leilao leilao = leilaoDao.porId(leilaoPlaystation.getId());
		
		assertThat(leilao, nullValue());
	}
	
}