package br.diego.leilaoonline.usuario.repository;

import br.diego.leilaoonline.usuario.model.Usuario;

public interface UsuarioRepository {

	void deletar(Usuario usuario);

	void atualizar(Usuario usuario);

	Long salvar(Usuario usuario);

	Usuario porNomeEEmail(String nome, String email);

	Usuario porId(Long id);

}
