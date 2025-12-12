package br.edu.utfpr.projetofinanceiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.entity.Transferencia;


public class TransferenciaDAO implements DAO<Transferencia, Integer> {
	private Connection conn;

	public TransferenciaDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public int cadastrar(Transferencia transferencia) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("insert into transferencia (id_usuario, id_conta_origem, id_conta_destino, valor, data_transferencia) values (?, ?, ?, ?, ?)");
			
			st.setInt(1, transferencia.getIdUsuario());
			st.setInt(2, transferencia.getIdContaOrigem());
			st.setInt(3, transferencia.getIdContaDestino());
			st.setDouble(4, transferencia.getValor());
			st.setDate(5, transferencia.getData());


			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
		}
	}

	@Override
	public List<Transferencia> buscarTodos() throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from transferencia order by data_transferencia");

			rs = st.executeQuery();

			List<Transferencia> listatransferencias = new ArrayList<>();

			while (rs.next()) {
				Transferencia transferencia = new Transferencia();

				transferencia.setIdTransferencia(rs.getInt("id_transferencia"));
				transferencia.setIdUsuario(rs.getInt("id_usuario"));
				transferencia.setIdContaOrigem(rs.getInt("id_conta_origem"));
				transferencia.setIdContaDestino(rs.getInt("id_conta_destino"));
				transferencia.setValor(rs.getDouble("valor"));
				transferencia.setData(rs.getDate("data_transferencia"));

				listatransferencias.add(transferencia);
			}

			return listatransferencias;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
		}
	}

	@Override
	public Transferencia buscarPorChave(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from transferencia where id_transferencia = ?");
			st.setInt(1, chavePrimaria);

			rs = st.executeQuery();

			if (rs.next()) {
				Transferencia transferencia = new Transferencia();

				transferencia.setIdTransferencia(rs.getInt("id_transferencia"));
				transferencia.setIdUsuario(rs.getInt("id_usuario"));
				transferencia.setIdContaOrigem(rs.getInt("id_conta_origem"));
				transferencia.setIdContaDestino(rs.getInt("id_conta_destino"));
				transferencia.setValor(rs.getDouble("valor"));
				transferencia.setData(rs.getDate("data_transferencia"));

				return transferencia;
			}

			return null;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
		}
	}

	@Override
	public int atualizar(Transferencia transferencia) throws SQLException {

		PreparedStatement st = null;

		try {
			
			st = conn.prepareStatement("update transferencia set id_conta_origem=?, id_conta_destino=?, valor=?, data_transferencia=? where id_transferencia=?");
			st.setInt(1, transferencia.getIdContaOrigem());
			st.setInt(2, transferencia.getIdContaDestino());
			st.setDouble(3, transferencia.getValor());
			st.setDate(4, transferencia.getData());
			st.setInt(5, transferencia.getIdTransferencia());

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
		}
	}

	@Override
	public int excluir(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("delete from transferencia where id_transferencia = ?");
			st.setInt(1, chavePrimaria);

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
		}
	}

}
