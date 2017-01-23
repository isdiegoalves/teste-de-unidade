package br.diego.leilaoonline.lance.entity;

import java.time.LocalDate;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.diego.leilaoonline.leilao.entity.Leilao;
import br.diego.leilaoonline.usuario.entity.Usuario;

@Entity
public class Lance {

	@Id @GeneratedValue
	private final Long id;
	private final LocalDate data;
	private final MonetaryAmount valor;
	@ManyToOne
	private final Usuario usuario;
	@ManyToOne
	private final Leilao leilao;
	
	public static LanceBuilder builder() {
		return new LanceBuilder();
	}

	public static Lance lance(Usuario usuario, MonetaryAmount valor, Leilao leilao) {
		return builder()
				.usuario(usuario)
				.valor(valor)
				.leilao(leilao)
				.build();
	}
	
	public static Lance lance(LocalDate data, Usuario usuario, MonetaryAmount valor, Leilao leilao) {
		return builder()
				.data(data)
				.usuario(usuario)
				.valor(valor)
				.leilao(leilao)
				.build();
	}

	protected Lance(LanceBuilder builder) {
		this.id 		= builder.getId();
		this.valor 		= builder.getValor();
		this.data 		= builder.getData();
		this.usuario 	= builder.getUsuario();
		this.leilao 	= builder.getLeilao();
	}

	public Long getId() {
		return id;
	}

	public Leilao getLeilao() {
		return leilao;
	}

	public LocalDate getData() {
		return data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public MonetaryAmount getValor() {
		return valor;
	}
	
	public LanceBuilder getLanceBuilder() {
		return builder()
				.id(this.id)
				.data(this.data)
				.valor(this.valor)
				.leilao(this.leilao)
				.usuario(this.usuario);
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
