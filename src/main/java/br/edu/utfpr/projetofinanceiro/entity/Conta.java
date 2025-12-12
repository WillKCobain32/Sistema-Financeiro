package br.edu.utfpr.projetofinanceiro.entity;

public class Conta {
    private int idConta;
    private int idUsuario; // FK
    private String nomeBanco;
    private String agencia;
    private String numeroConta;
    private double saldoInicial;
    private String tipoConta;

    public Conta() {}

    public Conta(int idConta, int idUsuario, String nomeBanco, String agencia, String numeroConta, double saldoInicial, String tipoConta) {
        this.idConta = idConta;
        this.idUsuario = idUsuario;
        this.nomeBanco = nomeBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldoInicial = saldoInicial;
        this.tipoConta = tipoConta;
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

	@Override
	public String toString() {
		return "Banco " + nomeBanco + " Conta " + numeroConta;
	}

    
}