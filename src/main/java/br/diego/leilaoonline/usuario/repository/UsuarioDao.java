package br.diego.leilaoonline.usuario.repository;

import org.hibernate.Session;

import br.diego.leilaoonline.usuario.entity.Usuario;

public class UsuarioDao {

	private final Session session;

	public UsuarioDao(Session session) {
		this.session = session;
	}

	public Usuario porId(Long id) {
		return (Usuario) session.load(Usuario.class, id);
	}

	public Usuario porNomeEEmail(String nome, String email) {
		return session.createQuery("from Usuario u where u.nome = :nome and u.email = :email", Usuario.class)
				.setParameter("nome", nome)
				.setParameter("email", email)
				.getSingleResult();
	}

	public void salvar(Usuario usuario) {
		session.save(usuario);
	}

	public void atualizar(Usuario usuario) {
		session.merge(usuario);
	}

	public void deletar(Usuario usuario) {
		session.delete(usuario);
	}
}
