package br.edu.utfpr.projetofinanceiro.entity;

import java.sql.Date;

public class Transferencia {
	private int idUsuario;
    private int idTransferencia;
    private int idContaOrigem;
    private int idContaDestino;
    private double valor;
    private Date data;
    private String categoria;
    private String tipo;

    public Transferencia() {}

    public Transferencia(int idTransferencia, int idUsuario, int idContaOrigem, int idContaDestino, double valor, Date data, String categoria, String tipo) {
        this.idTransferencia = idTransferencia;
        this.idUsuario = idUsuario;
        this.idContaOrigem = idContaOrigem;
        this.idContaDestino = idContaDestino;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.tipo = tipo;
    }

	public int getIdTransferencia() {
		return idTransferencia;
	}

	public void setIdTransferencia(int idTransferencia) {
		this.idTransferencia = idTransferencia;
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdContaOrigem() {
		return idContaOrigem;
	}

	public void setIdContaOrigem(int idContaOrigem) {
		this.idContaOrigem = idContaOrigem;
	}

	public int getIdContaDestino() {
		return idContaDestino;
	}

	public void setIdContaDestino(int idContaDestino) {
		this.idContaDestino = idContaDestino;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
    
}