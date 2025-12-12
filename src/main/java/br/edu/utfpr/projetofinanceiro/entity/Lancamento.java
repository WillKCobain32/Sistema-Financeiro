package br.edu.utfpr.projetofinanceiro.entity;

import java.sql.Date;

public class Lancamento {
	private int idLancamento;
	private int idUsuario;
	private int idConta;
	private int idCategoria;
	private String descricao;
	private double valor;
	private Date dataLancamento;
	private TipoLancamento tipo;
	private String fixo; //Sim ou Não
	
	public Lancamento() {
		
	}
	
	public Lancamento(int idLancamento, int idUsuario, int idConta, int idCategoria, String descricao, double valor, Date dataLancamento,
			String fixo, String tipo) {
		this.idLancamento = idLancamento;
		this.idUsuario = idUsuario;
		this.idConta = idConta;
		this.idCategoria = idCategoria;
		this.descricao = descricao;
		this.valor = valor;
		this.dataLancamento = dataLancamento;
		this.fixo = fixo;
	}
	
	public int getIdLancamento() {
		return idLancamento;
	}
	
	public void setIdLancamento(int idLancamento) {
		this.idLancamento = idLancamento;
	}


	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdConta() {
		return idConta;
	}

	public void setIdConta(int idConta) {
		this.idConta = idConta;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getFixo() {
		return fixo;
	}

	public void setFixo(String fixo) {
		this.fixo = fixo;
	}

	public boolean isDespesa() {
		
		return tipo == TipoLancamento.DESPESA;
		
	}
	
	public boolean IsReceita() {
		
		return tipo == TipoLancamento.RECEITA;
	}
	
}
