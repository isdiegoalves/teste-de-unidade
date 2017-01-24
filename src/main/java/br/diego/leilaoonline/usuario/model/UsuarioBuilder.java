package br.diego.leilaoonline.usuario.model;

public class UsuarioBuilder {

	private Long id;
	private String nome;
	private String email;

	public UsuarioBuilder nome(String nome) {
		if (nome == null)
			throw new NullPointerException("nome não pode ser nulo!");
		
		if (nome.trim().length() == 0)
			throw new IllegalArgumentException("nome não pode em branco!");
		
		if (nome.matches("^[ 0-9-].*"))
			throw new IllegalArgumentException("nome não pode iniciar com caracteres invalidos!");
		
		this.nome = nome;
		return this;
	}
	
	public UsuarioBuilder email(String email) {
		this.email = email;
		return this;
	}
	
	public Long getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	public String getEmail() {
		return this.email;
	}

	public UsuarioBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public Usuario build() {
		return new Usuario(this);
	}
}
