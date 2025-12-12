package br.edu.utfpr.projetofinanceiro.dao;

import br.edu.utfpr.projetofinanceiro.entity.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UsuarioDAOTeste {
	
	public static void cadastrarTeste() throws SQLException, IOException  {
		
		Usuario usuario =  new Usuario();
		
		usuario.setNomeCompleto("Nathan Santos");
		usuario.setDataNascimento(java.sql.Date.valueOf("1993-08-12"));
        usuario.setSexo("M");
        usuario.setNomeUsuario("nathan.santos");
        usuario.setSenha("pass123");

        Connection conn = BancoDados.conectar();
        int resultado = 0;
        try {
            resultado = new UsuarioDAO(conn).cadastrar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(resultado == 1){
            System.out.println("Usuario cadastrado com sucesso");
        }
        else{
            System.out.println("Erro ao cadastrar usuario");
        }

	}

    public static void BuscarTudoTeste() throws SQLException, IOException  {
        Connection conn = BancoDados.conectar();

        List<Usuario> usuarios = new UsuarioDAO(conn).buscarTodos();
        if(usuarios.isEmpty()){
            System.out.println("Nenhum usuario encontrado");
        }
        else{
            for(Usuario usuario : usuarios){
                System.out.println(usuario);
            }
        }



    }

    public static void buscarPorChaveTeste() throws SQLException, IOException {

        int chave = 1;

        Connection conn = BancoDados.conectar();
        Usuario usuario = new UsuarioDAO(conn).buscarPorChave(chave);

        if (usuario != null) {

            System.out.println(usuario);

        } else {

            System.out.println("Usuario não encontrado.");
        }
    }

    public static void atualizarTeste() throws SQLException, IOException {

        Usuario usuario =  new Usuario();

        usuario.setIdUsuario(1);
        usuario.setNomeCompleto("Nathan Ferreira");
        usuario.setDataNascimento(java.sql.Date.valueOf("1993-08-12"));
        usuario.setSexo("M");
        usuario.setNomeUsuario("nathan.ferreira");
        usuario.setSenha("pass123");

        Connection conn = BancoDados.conectar();
        int resultado = new UsuarioDAO(conn).atualizar(usuario);

        if (resultado > 0) {

            System.out.println("Usuario atualizado com sucesso.");

        } else {

            System.out.println("Erro ao atualizar um usuario.");
        }
    }

    public static void main(String[] args) {

        try {
//
            //UsuarioDAOTeste.cadastrarTeste();
            UsuarioDAOTeste.BuscarTudoTeste();
            //UsuarioDAOTeste.buscarPorChaveTeste();
            //UsuarioDAOTeste.atualizarTeste();

        } catch (SQLException | IOException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

}
