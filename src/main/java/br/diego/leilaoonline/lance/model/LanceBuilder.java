package br.diego.leilaoonline.lance.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LanceBuilder {

	private Long id;
	private Usuario usuario;
	private MonetaryAmount valor;
	private Leilao leilao;
	private LocalDate data = LocalDate.now();
	
	public LanceBuilder id(Long id) {
		this.id = id;
		return this;
	}
	
	public LanceBuilder usuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}
	
	public LanceBuilder valor(MonetaryAmount valor) {
		this.valor = valor;
		return this;
	}
	
	public LanceBuilder leilao(Leilao leilao) {
		this.leilao = leilao;
		return this;
	}
	
	public LanceBuilder data(LocalDate data) {
		this.data = data;
		return this;
	}

	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public MonetaryAmount getValor() {
		return valor;
	}

	public Leilao getLeilao() {
		return leilao;
	}

	public LocalDate getData() {
		return data;
	}
	
	public Lance build() {
		
		requireNonNull(usuario, "usuario nao pode ser nulo!");
		requireNonNull(valor, "valor nao pode ser nulo!");
		requireNonNull(leilao, "leilao nao pode ser nulo!");
		requireNonNull(data, "data nao pode ser nulo!");

		if (valor.isLessThanOrEqualTo(Money.of(0, valor.getCurrency()))) {
			throw new IllegalArgumentException("valor nao pode ser menor ou igual a zero!");
		}
		
		return new Lance(this);
	}
}
