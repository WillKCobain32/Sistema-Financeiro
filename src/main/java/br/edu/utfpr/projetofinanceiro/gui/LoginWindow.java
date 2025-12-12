package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.service.UsuarioService;

public class LoginWindow extends JFrame {

	
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtSenha;
	private JButton btnLogin;

	private UsuarioService usuarioService;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public LoginWindow() throws IOException, SQLException {
        this.inicializarComponentes();
        this.usuarioService = new UsuarioService();
	}

	private void realizarLogin() {
		String usuario = txtUsuario.getText().trim();
		String senha = new String(txtSenha.getPassword());

		if (usuario.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, 
				"Por favor, preencha todos os campos!", 
				"Campos Obrigatórios", 
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		Usuario usuarioAutenticado = null;
		
		try {
			usuarioAutenticado = usuarioService.autenticarUsuario(usuario, senha);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (usuarioAutenticado != null) {
			JOptionPane.showMessageDialog(this, 
				"Login realizado com sucesso!\nBem-vindo, " + usuarioAutenticado.getNomeCompleto(), 
				"Login Bem-sucedido", 
				JOptionPane.INFORMATION_MESSAGE);
			
			// Abrira a tela principal (Dashboard)
			abrirDashboard(usuarioAutenticado);
			this.dispose(); // Fecha a tela de login
			
		} else {
			JOptionPane.showMessageDialog(this, 
				"Usuário ou senha incorretos!", 
				"Erro de Autenticação", 
				JOptionPane.ERROR_MESSAGE);
			limparCampos();
		}
	}

	private void abrirDashboard(Usuario usuario) {
		TelaPrincipalWindow dashboard = new TelaPrincipalWindow(usuario);
	    dashboard.setVisible(true);
	    this.dispose();
	}

	private void limparCampos() {
		txtUsuario.setText("");
		txtSenha.setText("");
		txtUsuario.requestFocus();
	}
	
	private void abrirCadastroUsuario() {
	    CadastroUsuarioWindow cadastroWindow = new CadastroUsuarioWindow(this);
	    cadastroWindow.setVisible(true);
	    this.setVisible(false);
	}

	public void inicializarComponentes() {
		setTitle("Sistema Financeiro - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JLabel lblTitulo = new JLabel("Sistema Financeiro");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setBounds(141, 33, 154, 36);
		contentPane.add(lblTitulo);

	
		JLabel lblUsuario = new JLabel("Usuário:");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUsuario.setBounds(49, 101, 60, 20);
		contentPane.add(lblUsuario);

		txtUsuario = 	new JTextField();
		txtUsuario.setBounds(119, 100, 200, 25);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);

	
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(49, 141, 60, 20);
		contentPane.add(lblSenha);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(119, 140, 200, 25);
		contentPane.add(txtSenha);

	
		btnLogin = new JButton("Entrar");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarLogin();
			}
		});
		btnLogin.setBounds(180, 175, 90, 30);
		contentPane.add(btnLogin);
		
		JButton btnCadastrar = new JButton("Cadastrar Usuário");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCadastroUsuario();
			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btnCadastrar.setBounds(329, 223, 97, 30);
		contentPane.add(btnCadastrar);
			    
	}
}