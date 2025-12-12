package br.edu.utfpr.projetofinanceiro.entity;

public class Meta {
    private int idMeta;
    private int idUsuario;
    private String nome;
    private double valorTotal;
    private double valorMensal;
    private String dataInicio;
    private String dataFim;
    private String tipo;

    public Meta() {}

    public Meta(int idMeta, int idUsuario, String nome, double valorTotal, double valorMensal, String dataInicio, String dataFim, String tipo) {
        this.idMeta = idMeta;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.valorTotal = valorTotal;
        this.valorMensal = valorMensal;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.tipo = tipo;
    }

	public int getIdMeta() {
		return idMeta;
	}

	public void setIdMeta(int idMeta) {
		this.idMeta = idMeta;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(double valorMensal) {
		this.valorMensal = valorMensal;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String toString() {
        return nome + " (" + tipo + ") - R$ " + valorMensal + "/mês";
    }
	
    
    

}