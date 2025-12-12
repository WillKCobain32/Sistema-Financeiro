package br.edu.utfpr.projetofinanceiro.dao;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements DAO<Usuario, Integer> {

	private Connection conn;

	public UsuarioDAO(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public int cadastrar(Usuario usuario) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("insert into usuario (nome_completo, data_nascimento, sexo, nome_usuario, senha) values (?, ?, ?, ?, ?)");
			st.setString(1, usuario.getNomeCompleto());
			st.setDate(2, usuario.getDataNascimento());
			st.setString(3, usuario.getSexo());
			st.setString(4, usuario.getNomeUsuario());
			st.setString(5, usuario.getSenha());

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			//BancoDados.desconectar();
		}
	}

	@Override
	public List<Usuario> buscarTodos() throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from usuario order by nome_completo");

			rs = st.executeQuery();

			List<Usuario> listaUsuarios = new ArrayList<>();

			while (rs.next()) {
				Usuario usuario = new Usuario();

				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNomeCompleto(rs.getString("nome_completo"));
				usuario.setDataNascimento(rs.getDate("data_nascimento"));
				usuario.setSexo(rs.getString("sexo"));
				usuario.setNomeUsuario(rs.getString("nome_usuario"));
				usuario.setSenha(rs.getString("senha"));

				listaUsuarios.add(usuario);
			}

			return listaUsuarios;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			//BancoDados.desconectar();
		}
	}

	@Override
	public Usuario buscarPorChave(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			
			st = conn.prepareStatement("select * from usuario where id_usuario = ?");
			
			st.setInt(1, chavePrimaria);

			rs = st.executeQuery();

			if (rs.next()) {
				Usuario usuario = new Usuario();

				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNomeCompleto(rs.getString("nome_completo"));
				usuario.setDataNascimento(rs.getDate("data_nascimento"));
				usuario.setSexo(rs.getString("sexo"));
				usuario.setNomeUsuario(rs.getString("nome_usuario"));
				usuario.setSenha(rs.getString("senha"));

				return usuario;
			}

			return null;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			//BancoDados.desconectar();
		}
	}

	@Override
	public int atualizar(Usuario usuario) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("update usuario set nome_completo = ?, data_nascimento = ?, sexo = ?, nome_usuario = ?, senha = ? where id_usuario = ?");

			st.setString(1, usuario.getNomeCompleto());
			st.setDate(2, usuario.getDataNascimento());
			st.setString(3, usuario.getSexo());
			st.setString(4, usuario.getNomeUsuario());
			st.setString(5, usuario.getSenha());
			st.setInt(6, usuario.getIdUsuario());

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			//BancoDados.desconectar();
		}
	}

	
	@Override
	public int excluir(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("delete from usuario where id_usuario = ?");
			st.setInt(1, chavePrimaria);

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			//BancoDados.desconectar();
		}
	}

}
