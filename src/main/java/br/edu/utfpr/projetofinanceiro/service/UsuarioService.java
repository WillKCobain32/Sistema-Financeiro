package br.edu.utfpr.projetofinanceiro.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.dao.BancoDados;
import br.edu.utfpr.projetofinanceiro.dao.UsuarioDAO;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.UsuarioDuplicadoException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;

public class UsuarioService {
	
	private UsuarioDAO usuarioDAO;
	public UsuarioService(){
		
	}

	
	public int cadastrar(Usuario usuario) throws SQLException, IOException, UsuarioDuplicadoException, ValidacaoException {
		Connection conn = BancoDados.conectar();
		this.usuarioDAO = new UsuarioDAO(conn);
		
		if(usuarioDAO.buscarPorChave(usuario.getIdUsuario()) != null) {
			
			throw new UsuarioDuplicadoException("Usuario ja existe");
			
		}
		
		if(usuario.getSenha().length() < 6) {
			
			throw new ValidacaoException("senha deve conter pelo menos 6 digitos");
			
		}
		
		
		return new UsuarioDAO(conn).cadastrar(usuario);
		
		
		
	}
	
	public List<Usuario> buscarTodos() throws SQLException, IOException{
		
		
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).buscarTodos();
		
	
	}
	
	public Usuario buscarPorChave(int chavePrimaria) throws SQLException, IOException{
		
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).buscarPorChave(chavePrimaria);
		
	
	}
	
	public int atualizar(Usuario usuario) throws SQLException, IOException, ValidacaoException{
		
		
	if(usuario.getSenha().length() < 6) {
			
			throw new ValidacaoException("senha deve conter pelo menos 6 digitos");
			
		}
		
		
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).atualizar(usuario);
		
		
	
		
	}
	
	public int excluir(int chavePrimaria) throws SQLException, IOException{
		
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).excluir(chavePrimaria);
		
		

		
	}
	
	public Usuario autenticarUsuario(String nomeUsuario, String senha) throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		this.usuarioDAO = new UsuarioDAO(conn);
        
        try {
 
            List<Usuario> usuarios = usuarioDAO.buscarTodos();
            
            for (Usuario usuario : usuarios) {
                if (usuario.getNomeUsuario().equals(nomeUsuario) && usuario.getSenha().equals(senha)) {
                    return usuario;
                }
            }
            return null;
        } finally {
            BancoDados.desconectar();
        }
    }
	
	
}