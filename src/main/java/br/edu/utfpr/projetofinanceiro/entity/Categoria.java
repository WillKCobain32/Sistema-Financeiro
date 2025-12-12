package br.edu.utfpr.projetofinanceiro.entity;

public class Categoria {
    private int idCategoria;
    private String nome; 
    private String tipo;

    public Categoria() {}

    public Categoria(int idCategoria, String nome, String tipo) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String toString() {
        return tipo + " " + nome;
    }
}