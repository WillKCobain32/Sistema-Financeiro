package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.CategoriaDAO;
import br.edu.utfpr.projetofinanceiro.dao.ContaDAO;
import br.edu.utfpr.projetofinanceiro.dao.LancamentoDAO;
import br.edu.utfpr.projetofinanceiro.entity.Categoria;
import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Lancamento;
import br.edu.utfpr.projetofinanceiro.exception.SaldoInsuficienteException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;

public class LancamentoService {
    
    public LancamentoService() {
    }

    public int cadastrar(Lancamento lancamento) throws Exception {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false); // Inicia transação

            // Cria os DAOs com a mesma conexão
            LancamentoDAO lancDAO = new LancamentoDAO(conn);
            ContaDAO contaDAO = new ContaDAO(conn);
            CategoriaDAO categoriaDAO = new CategoriaDAO(conn);

            // Validações
            validarLancamento(lancamento, categoriaDAO, contaDAO);

            // Busca conta e aplica lançamento no saldo
            Conta conta = contaDAO.buscarPorChave(lancamento.getIdConta());
            aplicarLancamentoNoSaldo(lancamento, conta);

            // Atualiza conta e cadastra lançamento
            contaDAO.atualizar(conta);
            int resultado = lancDAO.cadastrar(lancamento);

            conn.commit(); // Confirma transação
            return resultado;

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // Rollback em caso de erro
            }
            throw e;
        } finally {
            if (conn != null) {
                BancoDados.desconectar(); // Fecha conexão no FINALLY
            }
        }
    }
    
    public List<Lancamento> buscarTodos() throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            LancamentoDAO lancDAO = new LancamentoDAO(conn);
            return lancDAO.buscarTodos();
        } finally {
            if (conn != null) {
                BancoDados.desconectar();
            }
        }
    }
    
    public List<Lancamento> buscarPorUsuario(int idUsuario) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            LancamentoDAO lancDAO = new LancamentoDAO(conn);
            List<Lancamento> todosLancamentos = lancDAO.buscarTodos();
            

            List<Lancamento> lancamentosUsuario = new ArrayList<>();
            for (Lancamento lancamento : todosLancamentos) {
                if (lancamento.getIdUsuario() == idUsuario) {
                    lancamentosUsuario.add(lancamento);
                }
            }
            return lancamentosUsuario;
        } finally {
            if (conn != null) {
                BancoDados.desconectar();
            }
        }
    }
    
    public Lancamento buscarPorChave(int chavePrimaria) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            LancamentoDAO lancDAO = new LancamentoDAO(conn);
            return lancDAO.buscarPorChave(chavePrimaria);
        } finally {
            if (conn != null) {
                BancoDados.desconectar();
            }
        }
    }
    
    public int atualizar(Lancamento lancamento) throws Exception {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);

            LancamentoDAO lancDAO = new LancamentoDAO(conn);
            ContaDAO contaDAO = new ContaDAO(conn);
            CategoriaDAO categoriaDAO = new CategoriaDAO(conn);

            validarLancamento(lancamento, categoriaDAO, contaDAO);
            Conta conta = contaDAO.buscarPorChave(lancamento.getIdConta());
            aplicarLancamentoNoSaldo(lancamento, conta);

            contaDAO.atualizar(conta);
            int resultado = lancDAO.atualizar(lancamento);

            conn.commit();
            return resultado;

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                BancoDados.desconectar();
            }
        }
    }
    
    public int excluir(int chavePrimaria) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            LancamentoDAO lancDAO = new LancamentoDAO(conn);
            return lancDAO.excluir(chavePrimaria);
        } finally {
            if (conn != null) {
                BancoDados.desconectar();
            }
        }
    }
        
    private void validarLancamento(Lancamento lancamento, CategoriaDAO categoriaDAO, ContaDAO contaDAO) 
            throws SQLException, ValidacaoException {
        
        if(lancamento.getValor() <= 0) {
            throw new ValidacaoException("O valor do lançamento deve ser maior que zero");
        }
        
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().isEmpty()) {
            throw new ValidacaoException("A descrição não pode estar vazia");
        }
        
        if(lancamento.getDataLancamento() == null) {
            throw new ValidacaoException("O lançamento deve ter uma data");            
        }
        
        validarCategoria(lancamento, categoriaDAO);
        validarConta(lancamento, contaDAO);
    }
    
    private void validarCategoria(Lancamento lancamento, CategoriaDAO categoriaDAO) 
            throws SQLException, ValidacaoException {
        
        if(lancamento.getIdCategoria() <= 0) {
            throw new ValidacaoException("ID da categoria é obrigatório");
        }
        
        Categoria categoria = categoriaDAO.buscarPorChave(lancamento.getIdCategoria());
        
        if(categoria == null) {
            throw new ValidacaoException("Categoria inexistente");
        }
        
        if (lancamento.isDespesa() && "RECEITA".equals(categoria.getTipo())) {
            throw new ValidacaoException("Categoria de receita não pode ser usada para despesa");
        }
        
        if (lancamento.IsReceita() && "DESPESA".equals(categoria.getTipo())) {
            throw new ValidacaoException("Categoria de despesa não pode ser usada para receita");
        }
    }
    
    private void aplicarLancamentoNoSaldo(Lancamento lancamento, Conta conta) throws SaldoInsuficienteException {
        double novoSaldo;

        if (lancamento.IsReceita()) {
            novoSaldo = conta.getSaldoInicial() + lancamento.getValor();
        } else { 
            if (conta.getSaldoInicial() < lancamento.getValor()) {
                throw new SaldoInsuficienteException("Saldo insuficiente");
            }
            novoSaldo = conta.getSaldoInicial() - lancamento.getValor();
        }

        conta.setSaldoInicial(novoSaldo);
    }
    
    private void validarConta(Lancamento lancamento, ContaDAO contaDAO) throws SQLException, ValidacaoException {
        Conta conta = contaDAO.buscarPorChave(lancamento.getIdConta());
        
        if(conta == null) {
            throw new ValidacaoException("Conta não existe");
        }
        
        if(lancamento.isDespesa() && conta.getSaldoInicial() < lancamento.getValor()) {
            throw new ValidacaoException("Saldo insuficiente para realizar esta despesa");            
        }
    }
}