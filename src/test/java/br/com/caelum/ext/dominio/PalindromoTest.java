package br.com.caelum.ext.dominio;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.ext.dominio.Palindromo;

public class PalindromoTest {

	@Test
    public void identicarPalindromoERemoverCaracteresInvalidos() {
        Palindromo palindromo = new Palindromo();
        
        Assert.assertTrue(palindromo.ehPalindromo("Anotaram a data da maratona"));
        
        Assert.assertTrue(palindromo.ehPalindromo("Socorram-me subi no onibus em Marrocos"));
    }
	
	@Test
    public void identicarNaoPalindromo() {
        Palindromo palindromo = new Palindromo();
        
        String frase = "Diego eh um cara bem legal";
        
        Assert.assertFalse(palindromo.ehPalindromo(frase));
    }
}