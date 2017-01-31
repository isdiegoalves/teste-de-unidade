package br.diego.leilaoonline.usuario.controller;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.diego.leilaoonline.usuario.model.Usuario;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;

public class UsuariosWSTest {

	private Usuario mauricio;
	private Usuario guilherme;

	@Before
	public void setUp() {

		mauricio = Usuario.builder()
				.id(1L)
				.nome("Mauricio Aniche")
				.email("mauricio.aniche@caelum.com.br")
				.build();

		guilherme = Usuario.builder()
				.id(2L)
				.nome("Guilherme Silveira")
				.email("guilherme.silveira@caelum.com.br")
				.build();
	} 
	
	@Test
	public void deveRetornarListaDeUsuarios() {
		XmlPath path = get("/usuarios?_format=xml").andReturn().xmlPath();

		Usuario usuario1 = path.getObject("list.usuario[0]", Usuario.class);
		Usuario usuario2 = path.getObject("list.usuario[1]", Usuario.class);
		
		assertThat(usuario1, equalTo(mauricio));
		assertThat(usuario2, equalTo(guilherme));
	}
	
	
	@Test
	public void deveRetornarListaDeUsuarios2() {
		XmlPath path = get("/usuarios?_format=xml").andReturn().xmlPath();
		
		List<Usuario> usuarios = path.getList("list.usuario", Usuario.class);
		
		assertThat(usuarios, hasSize(2));
		assertThat(usuarios, contains(mauricio, guilherme));
	}
	
	@Test
	public void deveRetornarListaUsuariosHeaderAcceptXML() {
		XmlPath path = given()
				.headers("Accept", "application/xml")
				.get("/usuarios?_format=xml")
				.andReturn().xmlPath();
		
		List<Usuario> usuarios = path.getList("list.usuario", Usuario.class);
		
		assertThat(usuarios, hasSize(2));
		assertThat(usuarios, contains(mauricio, guilherme));
	}
	
	@Test
	public void deveRetornarUsuariosHeaderAcceptJson() {
		JsonPath path = given()
				.queryParam("usuario.id", 1L)
				.headers("Accept", "application/json")
				.get("/usuarios/show")
				.andReturn().jsonPath();
		
		Usuario usuario = path.getObject("usuario", Usuario.class);
		
		assertThat(usuario, equalTo(mauricio));
	}
}
