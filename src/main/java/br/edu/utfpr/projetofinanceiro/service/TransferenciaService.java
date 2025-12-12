package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.ContaDAO;
import br.edu.utfpr.projetofinanceiro.dao.TransferenciaDAO;
import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Transferencia;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.SaldoInsuficienteException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;

public class TransferenciaService {
	
	public TransferenciaService() {
		
	}

	public List<Conta> buscarContasUsuario(Usuario usuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		try {
			ContaDAO contaDAO = new ContaDAO(conn);
			List<Conta> todasContas = contaDAO.buscarTodos();
			List<Conta> contasUsuario = new ArrayList<>();
			
			for (Conta conta : todasContas) {
				if (conta.getIdUsuario() == usuario.getIdUsuario()) {
					contasUsuario.add(conta);
				}
			}
			return contasUsuario;
		} finally {
		}
	}
	
	public String getNomeConta(int idConta) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		try {
			ContaDAO contaDAO = new ContaDAO(conn);
			Conta conta = contaDAO.buscarPorChave(idConta); 
			return conta != null ? conta.getNomeBanco() + " - " + conta.getNumeroConta() : "Conta não encontrada";
		} finally {
		}
	}

	public void realizarTransferencia(Transferencia transferencia, Usuario usuario) 
			throws ValidacaoException, SaldoInsuficienteException, SQLException, IOException {
		
		validarTransferencia(transferencia);
		
		Connection conn = BancoDados.conectar();
		try {
			TransferenciaDAO transferenciaDAO = new TransferenciaDAO(conn);
			ContaDAO contaDAO = new ContaDAO(conn);
			
			Conta contaOrigem = contaDAO.buscarPorChave(transferencia.getIdContaOrigem());
			Conta contaDestino = contaDAO.buscarPorChave(transferencia.getIdContaDestino());
			
			if (contaOrigem.getIdUsuario() != usuario.getIdUsuario() || 
				contaDestino.getIdUsuario() != usuario.getIdUsuario()) {
				throw new ValidacaoException("Contas devem pertencer ao usuário logado");
			}

			if (contaOrigem.getSaldoInicial() < transferencia.getValor()) {
				throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem");
			}
			
			contaOrigem.setSaldoInicial(contaOrigem.getSaldoInicial() - transferencia.getValor());
			contaDestino.setSaldoInicial(contaDestino.getSaldoInicial() + transferencia.getValor());
			
			contaDAO.atualizar(contaOrigem);
			contaDAO.atualizar(contaDestino);
			
			transferenciaDAO.cadastrar(transferencia);
		} finally {
		}
	}
	 
	public List<Transferencia> buscarTransferenciasUsuario(int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		try {
			List<Transferencia> todasTransferencias = new TransferenciaDAO(conn).buscarTodos();
			List<Transferencia> transferenciasUsuario = new ArrayList<>();

			for (Transferencia t : todasTransferencias) {
				if (pertenceAoUsuario(t, idUsuario)) {
					transferenciasUsuario.add(t);
				}
			}
			return transferenciasUsuario;
		} finally {
		}
	}
	
	public Transferencia buscarPorChave(int chavePrimaria) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		try {
			return new TransferenciaDAO(conn).buscarPorChave(chavePrimaria);
		} finally {
		}
	}
	
	public void excluirTransferencia(int idTransferencia, Usuario usuario) 
			throws SQLException, SaldoInsuficienteException, ValidacaoException, IOException {
		
		Connection conn = BancoDados.conectar();
		try {
			TransferenciaDAO transferenciaDAO = new TransferenciaDAO(conn);
			ContaDAO contaDAO = new ContaDAO(conn);
			
			Transferencia transferencia = transferenciaDAO.buscarPorChave(idTransferencia);
			if (transferencia == null) {
				throw new ValidacaoException("Transferência não encontrada");
			}
			
			Conta contaOrigem = contaDAO.buscarPorChave(transferencia.getIdContaOrigem());
			Conta contaDestino = contaDAO.buscarPorChave(transferencia.getIdContaDestino());
			
			if (contaOrigem.getIdUsuario() != usuario.getIdUsuario() || 
				contaDestino.getIdUsuario() != usuario.getIdUsuario()) {
				throw new ValidacaoException("Transferência não pertence ao usuário");
			}
			
			contaOrigem.setSaldoInicial(contaOrigem.getSaldoInicial() + transferencia.getValor());
			contaDestino.setSaldoInicial(contaDestino.getSaldoInicial() - transferencia.getValor());
			
			if (contaDestino.getSaldoInicial() < 0) {
				throw new SaldoInsuficienteException("Não é possível reverter - saldo da conta destino ficaria negativo");
			}
			
			contaDAO.atualizar(contaOrigem);
			contaDAO.atualizar(contaDestino);
			
			transferenciaDAO.excluir(idTransferencia);
		} finally {
		}
	}
	
	private boolean pertenceAoUsuario(Transferencia transferencia, int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		try {
			ContaDAO contaDAO = new ContaDAO(conn);
			
			Conta contaOrigem = contaDAO.buscarPorChave(transferencia.getIdContaOrigem());
 
			Conta contaDestino = contaDAO.buscarPorChave(transferencia.getIdContaDestino());
			
			return contaOrigem.getIdUsuario() == idUsuario || contaDestino.getIdUsuario() == idUsuario;
		} finally {
		}
	}
	
	private void validarTransferencia(Transferencia transferencia) throws ValidacaoException {
		
		if(transferencia.getValor() <= 0) {
			throw new ValidacaoException("O valor da transferência deve ser maior que 0");
		}
		
		if(transferencia.getIdContaOrigem() == transferencia.getIdContaDestino()) {
			throw new ValidacaoException("Conta origem e destino devem ser diferentes");
		}
		
		if(transferencia.getData() == null){
			throw new ValidacaoException("A data é obrigatória");
		}
	}
}