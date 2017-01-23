package br.diego.ext.dominio;

public class Palindromo {

    public boolean ehPalindromo(String frase) {

        String fraseFiltrada = frase.toLowerCase().replaceAll("[ -]", "");
        
        String fraseReversa = new StringBuilder(fraseFiltrada).reverse().toString();

        return fraseFiltrada.equals(fraseReversa);
    }
}