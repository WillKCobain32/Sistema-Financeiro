package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.ContaDAO;
import br.edu.utfpr.projetofinanceiro.dao.UsuarioDAO;
import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.SaldoInsuficienteException;
import br.edu.utfpr.projetofinanceiro.exception.UsuarioDuplicadoException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoUsuarioException;

public class ContaService {

	private ContaDAO contaDAO;
	public ContaService() {
		
	}
	
   public ContaService(UsuarioDAO usuarioDAO, ContaDAO contaDAO) {
		this.contaDAO = contaDAO;
		
	}
	
   
   public int cadastrar(Conta conta) throws SQLException, ValidacaoException, IOException {
	   Connection conn = BancoDados.conectar();
	   this.contaDAO = new ContaDAO(conn);
       try {
           if(conta.getSaldoInicial() < 0) {
               throw new ValidacaoException("O saldo inicial não deve ser negativo");
           }

           List<Conta> contas = contaDAO.buscarTodos();
           for (Conta c : contas) {
               if (c.getNumeroConta().equals(conta.getNumeroConta())) {
                   throw new ValidacaoException("Já existe uma conta com esse número");
               }
           }

           return contaDAO.cadastrar(conta);
       } finally {
           BancoDados.desconectar();
       }
   }
   
	
	public List<Conta> buscarTodos() throws SQLException, IOException{
		
		Connection conn = BancoDados.conectar();
		return new ContaDAO(conn).buscarTodos();
			
	}
	
	
	public List<Conta> buscarContasPorUsuario(int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
	    List<Conta> todasContas = new ContaDAO(conn).buscarTodos();
	    List<Conta> contasUsuario = new ArrayList<>();
	    for (Conta conta : todasContas) {
	    	if (conta.getIdUsuario() == idUsuario) {
	    		contasUsuario.add(conta);
	    	}
	    }
	    	return contasUsuario;
	        
	    }
	
	public Conta buscarPorChave(int chavePrimaria) throws SQLException, IOException{
		
		
		Connection conn = BancoDados.conectar();
		return new ContaDAO(conn).buscarPorChave(chavePrimaria);
		

	}
	
	public int atualizar(Conta conta) throws SQLException, IOException{
				
		Connection conn = BancoDados.conectar();
		return new ContaDAO(conn).atualizar(conta);
		
	}
	
	public int excluir(int chavePrimaria) throws SQLException, IOException{
	
		Connection conn = BancoDados.conectar();
		return new ContaDAO(conn).excluir(chavePrimaria);
		
		
	}
	
	
	public void transferir(int idContaOrigem, int idContaDestino, double valor) throws ValidacaoException, SQLException, ValidacaoUsuarioException, SaldoInsuficienteException {
		
		if(valor <= 0) {
			
			
			throw new ValidacaoException("O valor da transferencia nao pode ser menor ou igual a 0");
			
		}
		
		Conta origem = contaDAO.buscarPorChave(idContaOrigem);
		Conta destino = contaDAO.buscarPorChave(idContaDestino);
		
		if(origem == null || destino == null) {
			
			
			throw new ValidacaoException("Transfencia so podem ocorrer entre contas existentes");
			
			
		}
		
		if(origem.getIdUsuario() != destino.getIdUsuario()) {
			
			throw new ValidacaoUsuarioException("Transferencia so podem ocorrer entre contras do mesmo usuario");
			
		}
		
		
		if(origem.getSaldoInicial() < valor) {
			
			
			throw new SaldoInsuficienteException("Saldo insuficiente para a transferencia");
			
			
			
		}
		
		origem.setSaldoInicial(origem.getSaldoInicial() - valor);
		destino.setSaldoInicial(destino.getSaldoInicial() + valor);
		
		contaDAO.atualizar(origem);
		contaDAO.atualizar(destino);
		
	}
	
	
	
	
}
