package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.CategoriaDAO;
import br.edu.utfpr.projetofinanceiro.dao.ContaDAO;
import br.edu.utfpr.projetofinanceiro.dao.LancamentoDAO;
import br.edu.utfpr.projetofinanceiro.entity.Categoria;
import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Lancamento;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;

public class ResumoFinanceiroService {
    
    public ResumoFinanceiroService() {
    }
    
    public double getSaldoTotal(Usuario usuario) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        try {
            ContaDAO contaDAO = new ContaDAO(conn);
            List<Conta> todasContas = contaDAO.buscarTodos();
            
            double saldoTotal = 0.0;
            for (Conta conta : todasContas) {
                if (conta.getIdUsuario() == usuario.getIdUsuario()) {
                    saldoTotal += conta.getSaldoInicial();
                }
            }
            return saldoTotal;
        } finally {
        }
    }
    
    public double getReceitasMes(Usuario usuario) throws SQLException, IOException {
        return calcularTotalPorTipo(usuario, "RECEITA");
    }
    
    public double getDespesasMes(Usuario usuario) throws SQLException, IOException {
        return calcularTotalPorTipo(usuario, "DESPESA");
    }
    
    public double getSaldoMes(Usuario usuario) throws SQLException, IOException {
        double receitas = getReceitasMes(usuario);
        double despesas = getDespesasMes(usuario);
        return receitas - despesas;
    }
    

    private double calcularTotalPorTipo(Usuario usuario, String tipoCategoria) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        try {
            LancamentoDAO lancamentoDAO = new LancamentoDAO(conn);
            CategoriaDAO categoriaDAO = new CategoriaDAO(conn);
            
            List<Lancamento> todosLancamentos = lancamentoDAO.buscarTodos();
            List<Categoria> todasCategorias = categoriaDAO.buscarTodos();
            
            double total = 0.0;
            String mesAnoAtual = getMesAnoAtual();
            
            for (Lancamento lancamento : todosLancamentos) {
                if (lancamentoPertenceAoUsuario(lancamento, usuario) &&
                    isLancamentoDoMes(lancamento, mesAnoAtual)) {
                    String tipo = getTipoCategoria(lancamento.getIdCategoria(), todasCategorias);
                    if (tipo != null && tipo.equals(tipoCategoria)) {
                        total += lancamento.getValor();
                    }
                }
            }
            return total;
        } finally {
        }
    }
    
    private String getTipoCategoria(int idCategoria, List<Categoria> categorias) {
        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoria() == idCategoria) {
                return categoria.getTipo();
            }
        }
        return null;
    }

    private boolean lancamentoPertenceAoUsuario(Lancamento lancamento, Usuario usuario) throws SQLException, IOException {
        Connection conn = BancoDados.conectar();
        try {
            ContaDAO contaDAO = new ContaDAO(conn);
            Conta conta = contaDAO.buscarPorChave(lancamento.getIdConta());
            return conta != null && conta.getIdUsuario() == usuario.getIdUsuario();
        } finally {

        }
    }
    
    private boolean isLancamentoDoMes(Lancamento lancamento, String mesAnoAtual) {
        if (lancamento.getDataLancamento() == null) return false;
        
        LocalDate data = lancamento.getDataLancamento().toLocalDate();
        String mesAnoLancamento = data.getYear() + "-" + String.format("%02d", data.getMonthValue());
        
        return mesAnoLancamento.equals(mesAnoAtual);
    }
    
    private String getMesAnoAtual() {
        LocalDate hoje = LocalDate.now();
        return hoje.getYear() + "-" + String.format("%02d", hoje.getMonthValue());
    }
}