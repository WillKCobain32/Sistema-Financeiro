package br.edu.utfpr.projetofinanceiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.entity.Lancamento;

public class LancamentoDAO implements DAO<Lancamento, Integer> {
	
	private Connection conn;

    public LancamentoDAO(Connection conn) {
    	
        this.conn = conn;
        
    }

    @Override
    public int cadastrar(Lancamento lancamento) throws SQLException {
        PreparedStatement st = null;
        try {
        	
            st = conn.prepareStatement("insert into lancamento (id_usuario, id_conta, id_categoria, descricao, valor, data_lancamento, fixo) values (?, ?, ?, ?, ?, ?, ?)");
            
            st.setInt(1, lancamento.getIdUsuario());
            st.setInt(2, lancamento.getIdConta());
            st.setInt(3, lancamento.getIdCategoria());
            st.setString(4, lancamento.getDescricao());
            st.setDouble(5, lancamento.getValor());
            st.setDate(6, lancamento.getDataLancamento());
            st.setString(7, lancamento.getFixo());
            
            return st.executeUpdate();
            
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }

    @Override
    public List<Lancamento> buscarTodos() throws SQLException {
    	
        PreparedStatement st = null;
        
        ResultSet rs = null;
        
        try {
            st = conn.prepareStatement("select * from lancamento order by valor");
            
            rs = st.executeQuery();
            
            List<Lancamento> lista = new ArrayList<>();
            
            while (rs.next()) {
            	
                Lancamento lancamento = new Lancamento();
                
                lancamento.setIdLancamento(rs.getInt("id_lancamento"));
                lancamento.setIdUsuario(rs.getInt("id_usuario"));
                lancamento.setIdConta(rs.getInt("id_conta"));
                lancamento.setIdCategoria(rs.getInt("id_categoria"));
                lancamento.setDescricao(rs.getString("descricao"));
                lancamento.setValor(rs.getDouble("valor"));
                lancamento.setDataLancamento(rs.getDate("data_lancamento"));
                lancamento.setFixo(rs.getString("fixo"));

                
                lista.add(lancamento);
            }
            return lista;
        } finally {
        	
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
           
            
        }
    }

    @Override
    public Lancamento buscarPorChave(Integer id) throws SQLException {
    	
        PreparedStatement st = null;
        
        ResultSet rs = null;
        
        try {
        	
            st = conn.prepareStatement("select * from lancamento where id_lancamento = ?");
            
            st.setInt(1, id);
            
            rs = st.executeQuery();
            
            if (rs.next()) {
                Lancamento lancamento = new Lancamento();
                
                lancamento.setIdLancamento(rs.getInt("id_lancamento"));
                lancamento.setIdUsuario(rs.getInt("id_usuario"));
                lancamento.setIdConta(rs.getInt("id_conta"));
                lancamento.setIdCategoria(rs.getInt("id_categoria"));
                lancamento.setDescricao(rs.getString("descricao"));
                lancamento.setValor(rs.getDouble("valor"));
                lancamento.setDataLancamento(rs.getDate("data_lancamento"));
                lancamento.setFixo(rs.getString("fixo"));
                
                return lancamento;
            }
            
            return null;
        } finally {
        	
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }
    }

    @Override
    public int atualizar(Lancamento lancamento) throws SQLException {
    	
        PreparedStatement st = null;
        
        try {
            st = conn.prepareStatement("update lancamento set id_categoria=?, descricao=?, valor=?, data_lancamento=?, fixo=? where id_lancamento=?");
            
            st.setInt(1, lancamento.getIdCategoria());
            st.setString(2, lancamento.getDescricao());
            st.setDouble(3, lancamento.getValor());
            st.setDate(4, lancamento.getDataLancamento());
            st.setString(5, lancamento.getFixo());
            st.setInt(6, lancamento.getIdLancamento());
            
            return st.executeUpdate();
        } finally {
        	
            BancoDados.finalizarStatement(st);
        }
    }

    @Override
    public int excluir(Integer id) throws SQLException {
        
    	PreparedStatement st = null;
        
        try {
            st = conn.prepareStatement("delete from lancamento where id_lancamento = ?");
            
            st.setInt(1, id);
            
            return st.executeUpdate();
            
        } finally {
        	
            BancoDados.finalizarStatement(st);

        }
    }	

}
