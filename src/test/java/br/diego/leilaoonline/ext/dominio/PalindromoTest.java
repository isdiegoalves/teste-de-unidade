package br.diego.leilaoonline.ext.dominio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.diego.ext.dominio.Palindromo;

public class PalindromoTest {

	@Test
    public void identicarPalindromoERemoverCaracteresInvalidos() {
        Palindromo palindromo = new Palindromo();
        
        assertTrue(palindromo.ehPalindromo("Anotaram a data da maratona"));
        assertTrue(palindromo.ehPalindromo("Socorram-me subi no onibus em Marrocos"));
    }
	
	@Test
    public void identicarNaoPalindromo() {
        Palindromo palindromo = new Palindromo();
        
        String frase = "Diego eh um cara bem legal";
        
        assertFalse(palindromo.ehPalindromo(frase));
    }
}