package br.edu.utfpr.projetofinanceiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.entity.Categoria;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;

public class CategoriaDAO implements DAO<Categoria, Integer> {

	private Connection conn;

	public CategoriaDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public int cadastrar(Categoria categoria) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("insert into categoria (nome_categoria, tipo) values (?, ?)");
			st.setString(1, categoria.getNome());
			st.setString(2, categoria.getTipo());

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
		}
	}
	
	
	@Override
	public List<Categoria> buscarTodos() throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from categoria order by nome_categoria");

			rs = st.executeQuery();

			List<Categoria> lista = new ArrayList<>();

			while (rs.next()) {
				Categoria categoria = new Categoria();

				categoria.setIdCategoria(rs.getInt("id_categoria"));
				categoria.setNome(rs.getString("nome_categoria"));
				categoria.setTipo(rs.getString("tipo"));

				lista.add(categoria);
			}

			return lista;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			
		}
	}

	@Override
	public Categoria buscarPorChave(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from categoria where id_categoria = ?");
			st.setInt(1, chavePrimaria);

			rs = st.executeQuery();

			if (rs.next()) {
				Categoria categoria = new Categoria();

				categoria.setIdCategoria(rs.getInt("id_categoria"));
				categoria.setNome(rs.getString("nome_categoria"));
				categoria.setTipo(rs.getString("tipo"));

				return categoria;
			}

			return null;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			
		}
	}
	
	
	@Override
	public int atualizar(Categoria categoria) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("update categoria set nome_categoria = ?, tipo = ? where id_categoria = ?");
			st.setString(1, categoria.getNome());
			st.setString(2, categoria.getTipo());
			st.setInt(3, categoria.getIdCategoria());

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			
		}
	}

	@Override
	public int excluir(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("delete from categoria where id_categoria = ?");
			st.setInt(1, chavePrimaria);

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
	
		}
	}
}
