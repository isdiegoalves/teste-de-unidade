package br.com.caelum.leilao.dominio;

import static java.util.Objects.requireNonNull;

import javax.money.MonetaryAmount;

public class Lance {

	private Usuario usuario;
	private MonetaryAmount valor;
	
	public static Lance lance(Usuario usuario, MonetaryAmount valor) {
		requireNonNull(usuario, "usuario nao pode ser nulo!");
		requireNonNull(valor, "valor nao pode ser nulo!");
		return new Lance(usuario, valor);
	}
	
	private Lance(Usuario usuario, MonetaryAmount valor) {
		this.usuario = usuario;
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public MonetaryAmount getValor() {
		return valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Lance))
			return false;
		Lance other = (Lance) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
}
