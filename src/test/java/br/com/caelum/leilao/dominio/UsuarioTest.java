package br.com.caelum.leilao.dominio;

import static br.com.caelum.leilao.dominio.Usuario.usuario;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

public class UsuarioTest {

	@Test(expected=NullPointerException.class)
	public void naoDevePermitirUsuarioSemNome() {
		usuario(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void naoDevePermitirUsuarioComNomeEmBranco() {
		usuario("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void naoDevePermitirUsuarioComNomeIniciadoComCaracteresInvalidos() {
		usuario(" Diego");
	}
	
	@Test
	public void devePermitirUsuarioComNomePreenchido() {
		Usuario usuario = usuario("Diego");
		
		MatcherAssert.assertThat(usuario.getNome(), equalTo("Diego"));
	}
}

