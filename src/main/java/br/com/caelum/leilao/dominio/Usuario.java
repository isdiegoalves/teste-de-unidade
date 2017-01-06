package br.com.caelum.leilao.dominio;

public class Usuario {

	private int id;
	private String nome;
	
	private Usuario(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public static Usuario usuario(String nome) {
		if (nome == null)
			throw new NullPointerException("nome não pode ser nulo!");
		
		if (nome.trim().length() == 0)
			throw new IllegalArgumentException("nome não pode em branco!");
		
		if (nome.matches("^[ 0-9-].*"))
			throw new IllegalArgumentException("nome não pode iniciar com caracteres invalidos!");
		
		return new Usuario(0, nome);
	}
	
	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
	
}
