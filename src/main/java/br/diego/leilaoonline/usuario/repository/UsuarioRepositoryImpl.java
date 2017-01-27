package br.diego.leilaoonline.usuario.repository;

import org.hibernate.Session;

import br.diego.leilaoonline.usuario.model.Usuario;

public class UsuarioRepositoryImpl implements UsuarioRepository {

	private final Session session;

	public UsuarioRepositoryImpl(Session session) {
		this.session = session;
	}

	@Override
	public Usuario porId(Long id) {
		return (Usuario) session.get(Usuario.class, id);
	}

	@Override
	public Usuario porNomeEEmail(String nome, String email) {
		return session.createQuery("select u from Usuario u where u.nome = :nome and u.email = :email", Usuario.class)
				.setParameter("nome", nome)
				.setParameter("email", email)
				.getSingleResult();
	}

	@Override
	public Long salvar(Usuario usuario) {
		Long idUsuario = (Long) session.save(usuario);
		return idUsuario;
	}

	@Override
	public void atualizar(Usuario usuario) {
		session.merge(usuario);
	}

	@Override
	public void deletar(Usuario usuario) {
		session.delete(usuario);
	}
}
