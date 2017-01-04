package br.com.caelum.leilao.dominio;

import static java.util.Objects.requireNonNull;

import javax.money.MonetaryAmount;

public class Lance {

	private Usuario usuario;
	private MonetaryAmount valor;
	
	public Lance(Usuario usuario, MonetaryAmount valor) {
		requireNonNull(usuario, "usuario nao pode ser nulo!");
		requireNonNull(valor, "valor nao pode ser nulo!");
		
		this.usuario = usuario;
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public MonetaryAmount getValor() {
		return valor;
	}
	
	
	
}
