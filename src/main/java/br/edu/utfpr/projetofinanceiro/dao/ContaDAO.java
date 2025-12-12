package br.edu.utfpr.projetofinanceiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.entity.Conta;

public class ContaDAO implements DAO<Conta, Integer> {

	private Connection conn;

	public ContaDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public int cadastrar(Conta conta) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO conta (id_usuario, nome_banco, agencia, numero_conta, saldo_inicial, tipo_conta) VALUES (?, ?, ?, ?, ?, ?)");

			st.setInt(1, conta.getIdUsuario());
			st.setString(2, conta.getNomeBanco());
			st.setString(3, conta.getAgencia());
			st.setString(4, conta.getNumeroConta());
			st.setDouble(5, conta.getSaldoInicial());
			st.setString(6, conta.getTipoConta());

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			//BancoDados.desconectar();
		}
	}

	@Override
	public List<Conta> buscarTodos() throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from conta order by nome_banco");

			rs = st.executeQuery();

			List<Conta> listaContas = new ArrayList<>();

			while (rs.next()) {
				Conta conta = new Conta();

				conta.setIdConta(rs.getInt("id_conta"));
				conta.setIdUsuario(rs.getInt("id_usuario"));
				conta.setNomeBanco(rs.getString("nome_banco"));
				conta.setAgencia(rs.getString("agencia"));
				conta.setNumeroConta(rs.getString("numero_conta"));
				conta.setSaldoInicial(rs.getDouble("saldo_inicial"));
				conta.setTipoConta(rs.getString("tipo_conta"));

				listaContas.add(conta);
			}

			return listaContas;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			//BancoDados.desconectar();
		}
	}

	@Override
	public Conta buscarPorChave(Integer chavePrimaria) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from conta where id_conta = ?");
			st.setInt(1, chavePrimaria);

			rs = st.executeQuery();

			if (rs.next()) {
				Conta conta = new Conta();

				conta.setIdConta(rs.getInt("id_conta"));
				conta.setIdUsuario(rs.getInt("id_usuario"));
				conta.setNomeBanco(rs.getString("nome_banco"));
				conta.setAgencia(rs.getString("agencia"));
				conta.setNumeroConta(rs.getString("numero_conta"));
				conta.setSaldoInicial(rs.getDouble("saldo_inicial"));
				conta.setTipoConta(rs.getString("tipo_conta"));

				return conta;
			}

			return null;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			//BancoDados.desconectar();
		}
	}

	@Override
	public int atualizar(Conta conta) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("update conta set id_usuario = ?, nome_banco = ?, agencia = ?, numero_conta = ?, saldo_inicial = ?, tipo_conta = ? where id_conta = ?");

			st.setInt(1, conta.getIdUsuario());
			st.setString(2, conta.getNomeBanco());
			st.setString(3, conta.getAgencia());
			st.setString(4, conta.getNumeroConta());
			st.setDouble(5, conta.getSaldoInicial());
			st.setString(6, conta.getTipoConta());
			st.setInt(7, conta.getIdConta());

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
			st = conn.prepareStatement("delete from conta where id_conta = ?");
			st.setInt(1, chavePrimaria);

			return st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			//BancoDados.desconectar();
		}
	}
}
