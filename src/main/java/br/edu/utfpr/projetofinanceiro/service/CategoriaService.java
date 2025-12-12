package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.CategoriaDAO;
import br.edu.utfpr.projetofinanceiro.entity.Categoria;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;

public class CategoriaService {
	 public CategoriaService() {
			
			
		}
		
		public int cadastrar(Categoria categoria) throws SQLException, IOException, ValidacaoException {
			
			validarCategoria(categoria);
			
		Connection conn = BancoDados.conectar();
		return new CategoriaDAO(conn).cadastrar(categoria);
			
			
		}
		
		public List<Categoria> buscarTodos() throws SQLException, IOException{
			
			Connection conn = BancoDados.conectar();
			return new CategoriaDAO(conn).buscarTodos();
			
		}
		
		public Categoria buscarPorChave(int chavePrimaria) throws SQLException, IOException{
			
			Connection conn = BancoDados.conectar();
			return new CategoriaDAO(conn).buscarPorChave(chavePrimaria);
			
		}
		
		public int atualizar(Categoria categoria) throws SQLException, IOException{
			
			Connection conn = BancoDados.conectar();
			return new CategoriaDAO(conn).atualizar(categoria);
			
		}
		
		public int excluir(int chavePrimaria) throws SQLException, IOException{
			
		
			Connection conn = BancoDados.conectar();
			return new CategoriaDAO(conn).excluir(chavePrimaria);
			
			
		}
		
		private void validarCategoria(Categoria categoria) throws ValidacaoException {
			
			if(categoria.getIdCategoria() < 0) {
				throw new ValidacaoException("Id da categoria deve ser obrigatoria");
				
			}
			
			if(categoria.getTipo() == null) {
				
				throw new ValidacaoException("Tipo da categoria deve ser (RECEITA / DESPESA / AMBOS");
				
			}
			
			
		}
		
		
		
}
