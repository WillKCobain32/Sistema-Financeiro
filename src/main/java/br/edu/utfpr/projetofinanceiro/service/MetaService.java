package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.MetaDAO;
import br.edu.utfpr.projetofinanceiro.entity.Meta;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;

public class MetaService {
	
	public MetaService() {}
	
	public void cadastrar(Meta meta) throws ValidacaoException, SQLException, IOException {
		validarMeta(meta);
		
		Connection conn = BancoDados.conectar();
		new MetaDAO(conn).cadastrar(meta);
	}
	
	public void atualizar(Meta meta) throws ValidacaoException, SQLException, IOException {
		validarMeta(meta);
		
		Connection conn = BancoDados.conectar();
		new MetaDAO(conn).atualizar(meta);
	}
	
	public void excluir(int id) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new MetaDAO(conn).excluir(id);
	}
	
	public Meta buscarPorChave(int id) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new MetaDAO(conn).buscarPorChave(id);
	}
	
	public List<Meta> buscarTodos() throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new MetaDAO(conn).buscarTodos();
	}
	
	private void validarMeta(Meta meta) throws ValidacaoException {
		
		if(meta.getNome() == null || meta.getNome().isEmpty()) {
			throw new ValidacaoException("O nome não pode estar vazio");			
		}
		
		if(meta.getValorMensal() <= 0) {
			throw new ValidacaoException("O valor mensal da meta deve ser maior que zero");
		}
		
		if(meta.getDataInicio() == null || meta.getDataInicio().isEmpty()) {
			throw new ValidacaoException("A data de início não pode estar vazia");
		}
		
		if(meta.getTipo() == null) {
			throw new ValidacaoException("O tipo da meta é obrigatório");
		}
		
		if("LONGO_PRAZO".equals(meta.getTipo()) && 
		  (meta.getDataFim() == null || meta.getDataFim().isEmpty())) {
			throw new ValidacaoException("Metas de longo prazo devem ter data de término");
		}
	}
}