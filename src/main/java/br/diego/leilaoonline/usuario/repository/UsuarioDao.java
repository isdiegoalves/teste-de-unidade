package br.diego.leilaoonline.usuario.repository;

import org.hibernate.Session;

import br.diego.leilaoonline.usuario.model.Usuario;

public class UsuarioDao {

	private final Session session;

	public UsuarioDao(Session session) {
		this.session = session;
	}

	public Usuario porId(Long id) {
		return (Usuario) session.get(Usuario.class, id);
	}

	public Usuario porNomeEEmail(String nome, String email) {
		return session.createQuery("select u from Usuario u where u.nome = :nome and u.email = :email", Usuario.class)
				.setParameter("nome", nome)
				.setParameter("email", email)
				.getSingleResult();
	}

	public Long salvar(Usuario usuario) {
		Long idUsuario = (Long) session.save(usuario);
		return idUsuario;
	}

	public void atualizar(Usuario usuario) {
		session.merge(usuario);
	}

	public void deletar(Usuario usuario) {
		session.delete(usuario);
	}
}
