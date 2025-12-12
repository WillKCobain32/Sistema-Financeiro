package br.edu.utfpr.projetofinanceiro.entity;

import java.sql.Date;

public class Usuario {
    private int idUsuario;
    private String nomeCompleto;
    private Date dataNascimento;
    private String sexo;
    private String nomeUsuario;
    private String senha;

    public Usuario() {}

    public Usuario(int idUsuario, String nomeCompleto, Date dataNascimento, String sexo, String nomeUsuario, String senha) {
        this.idUsuario = idUsuario;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

	@Override
	public String toString() {
		return "Usuario [nomeUsuario=" + nomeUsuario + ", senha=" + senha + "]";
	}

    
}