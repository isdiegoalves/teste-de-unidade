package br.diego.leilaoonline.ext.dominio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.diego.ext.dominio.AnoBissexto;

public class AnoBissextoTest {

	private AnoBissexto ano;
	
	@Before
	public void before() {
		ano = new AnoBissexto();
	}

	@Test
	public void deveSerAnoBissexto() {
		assertTrue(ano.ehBissexto(2004));
		assertTrue(ano.ehBissexto(2008));
		assertTrue(ano.ehBissexto(2012));
		assertTrue(ano.ehBissexto(2016));
		assertTrue(ano.ehBissexto(2100));
		assertTrue(ano.ehBissexto(400));
	}
	
	@Test
	public void naoDeveSerAnoBissexto() {
		assertFalse(ano.ehBissexto(2017));
	}
}
