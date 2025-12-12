package br.edu.utfpr.projetofinanceiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.entity.Meta;

public class MetaDAO implements DAO<Meta, Integer> {

    private Connection conn;

    public MetaDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int cadastrar(Meta meta) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "INSERT INTO meta (id_usuario, nome_meta, tipo_meta, valor_total, valor_mensal, data_inicio, data_fim) VALUES (?, ?, ?, ?, ?, ?, ?)");
            
            st.setInt(1, meta.getIdUsuario());
            st.setString(2, meta.getNome()); 
            st.setString(3, meta.getTipo()); 
            st.setDouble(4, meta.getValorTotal());
            st.setDouble(5, meta.getValorMensal());
            st.setString(6, meta.getDataInicio());
            st.setString(7, meta.getDataFim());
            
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }

    @Override
    public List<Meta> buscarTodos() throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM meta ORDER BY nome_meta");
            rs = st.executeQuery();
            
            List<Meta> lista = new ArrayList<>();
            
            while (rs.next()) {
                Meta meta = new Meta();
                
                meta.setIdMeta(rs.getInt("id_meta"));
                meta.setIdUsuario(rs.getInt("id_usuario"));
                meta.setNome(rs.getString("nome_meta"));      // nome_meta
                meta.setTipo(rs.getString("tipo_meta"));      // tipo_meta
                meta.setValorTotal(rs.getDouble("valor_total"));
                meta.setValorMensal(rs.getDouble("valor_mensal"));
                meta.setDataInicio(rs.getString("data_inicio"));
                meta.setDataFim(rs.getString("data_fim"));
                
                lista.add(meta);
            }
            return lista;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }
    }

    @Override
    public Meta buscarPorChave(Integer id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM meta WHERE id_meta = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                Meta meta = new Meta();
                meta.setIdMeta(rs.getInt("id_meta"));
                meta.setIdUsuario(rs.getInt("id_usuario"));
                meta.setNome(rs.getString("nome_meta"));
                meta.setTipo(rs.getString("tipo_meta"));
                meta.setValorTotal(rs.getDouble("valor_total"));
                meta.setValorMensal(rs.getDouble("valor_mensal"));
                meta.setDataInicio(rs.getString("data_inicio"));
                meta.setDataFim(rs.getString("data_fim"));
                
                return meta;
            }
            return null;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
 
        }
    }

    @Override
    public int atualizar(Meta meta) throws SQLException {
        PreparedStatement st = null;
        try {
            // ORDEM CORRETA para UPDATE
            st = conn.prepareStatement(
                "UPDATE meta SET nome_meta=?, tipo_meta=?, valor_total=?, valor_mensal=?, data_inicio=?, data_fim=? WHERE id_meta=?");
            
            st.setString(1, meta.getNome()); 
            st.setString(2, meta.getTipo());
            st.setDouble(3, meta.getValorTotal());
            st.setDouble(4, meta.getValorMensal());
            st.setString(5, meta.getDataInicio());
            st.setString(6, meta.getDataFim());
            st.setInt(7, meta.getIdMeta());
            
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }

    @Override
    public int excluir(Integer id) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM meta WHERE id_meta = ?");
            st.setInt(1, id);
            return st.executeUpdate();
        } finally {
        }
    }
}