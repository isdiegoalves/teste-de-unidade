package br.com.caelum.ext.dominio;

import org.junit.Assert;
import org.junit.Test;

public class AnoBissextoTest {

	@Test
	public void deveSerAnoBissexto() {
		
		AnoBissexto ano = new AnoBissexto();
		
		Assert.assertTrue(ano.ehBissexto(2004));
		Assert.assertTrue(ano.ehBissexto(2008));
		Assert.assertTrue(ano.ehBissexto(2012));
		Assert.assertTrue(ano.ehBissexto(2016));
		Assert.assertTrue(ano.ehBissexto(2100));
		Assert.assertTrue(ano.ehBissexto(400));
	}
	
	@Test
	public void naoDeveSerAnoBissexto() {
		
		AnoBissexto ano = new AnoBissexto();
		
		Assert.assertFalse(ano.ehBissexto(2017));
	}
}
