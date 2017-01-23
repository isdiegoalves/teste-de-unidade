package br.diego.leilaoonline.usuario.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Usuario {
	
	@Id @GeneratedValue
	private Long id;
	private String nome;
	private String email;
	
	protected Usuario() {}
	
	public static Usuario fromId(Long id) {
		Usuario u = new Usuario();
		u.id = id;
		return u;
	}
	
	public static Usuario usuario(String nome, String email) {
		if (nome == null)
			throw new NullPointerException("nome não pode ser nulo!");
		
		if (nome.trim().length() == 0)
			throw new IllegalArgumentException("nome não pode em branco!");
		
		if (nome.matches("^[ 0-9-].*"))
			throw new IllegalArgumentException("nome não pode iniciar com caracteres invalidos!");
		
		Usuario usuario = new Usuario();
		usuario.nome = nome;
		usuario.email = email;
		return usuario;
	}
	
	public static Usuario usuario(String nome) {
		if (nome == null)
			throw new NullPointerException("nome não pode ser nulo!");
		
		if (nome.trim().length() == 0)
			throw new IllegalArgumentException("nome não pode em branco!");
		
		if (nome.matches("^[ 0-9-].*"))
			throw new IllegalArgumentException("nome não pode iniciar com caracteres invalidos!");
		
		Usuario usuario = new Usuario();
		usuario.nome = nome;
		return usuario;
	}
	
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
	
	public String getEmail() {
		return email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}