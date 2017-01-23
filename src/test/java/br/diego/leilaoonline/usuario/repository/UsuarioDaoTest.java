package br.diego.leilaoonline.usuario.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.diego.leilaoonline.infra.persistence.hibernate.CriadorDeSessao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class UsuarioDaoTest {
	
	private Session session;
	private UsuarioDao usuarioDao;
	
	@Test
	public void deveEncontrarPeloNomeEEmail() {

		Usuario usuarioBanco = usuarioDao.porNomeEEmail("diego", "diego@alves.com");
		
		assertThat(usuarioBanco, notNullValue());
		assertThat(usuarioBanco.getNome(),  equalTo("diego"));
		assertThat(usuarioBanco.getEmail(), equalTo("diego@alves.com"));
	}
	
	@Test(expected=NoResultException.class)
	public void naoDeveEncontrarPeloNomeEEmail() {

		usuarioDao.porNomeEEmail("debora", "debora@alves.com");
	}

	@Before
	public void before() {
		session = new CriadorDeSessao().session();
		session.beginTransaction();
		usuarioDao = new UsuarioDao(session);
		
	}
	
	@After
	public void after() {
		session.getTransaction().rollback();
		session.close();
	}
}
