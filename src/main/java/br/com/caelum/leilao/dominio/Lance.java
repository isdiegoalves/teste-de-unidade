package br.com.caelum.leilao.dominio;

import javax.money.MonetaryAmount;

public class Lance {

	private Usuario usuario;
	private MonetaryAmount valor;
	
	public Lance(Usuario usuario, MonetaryAmount valor) {
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
