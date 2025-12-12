package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.UsuarioDuplicadoException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.service.UsuarioService;

public class CadastroUsuarioWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNomeCompleto;
	private JTextField txtDataNascimento;
	private JComboBox<String> cbSexo;
	private JTextField txtNomeUsuario;
	private JPasswordField txtSenha;
	private JPasswordField txtConfirmarSenha;
	private JButton btnCadastrar;
	private JButton btnVoltar;
	private MaskFormatter mascaraData;

	private LoginWindow loginWindow;
	private UsuarioService usuarioService;

	public CadastroUsuarioWindow(LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		this.usuarioService = new UsuarioService();
		this.criarMascaraData();
		this.inicializarComponentes();
	}

	private void criarMascaraData() {
		try {
			this.mascaraData = new MaskFormatter("##/##/####");
			this.mascaraData.setPlaceholderCharacter('_');
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null,"Erro: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cadastrarUsuario() {
		try {
			if (!validarCampos()) {
				return;
			}

			if (!validarSenhas()) {
				JOptionPane.showMessageDialog(null, 
					"As senhas não coincidem!", 
					"Erro de Validação", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			Usuario usuario = new Usuario();
			usuario.setNomeCompleto(txtNomeCompleto.getText().trim());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date dataUtil = sdf.parse(txtDataNascimento.getText());
			usuario.setDataNascimento(new Date(dataUtil.getTime()));
			
			usuario.setSexo(cbSexo.getSelectedItem().toString());
			usuario.setNomeUsuario(txtNomeUsuario.getText().trim());
			usuario.setSenha(new String(txtSenha.getPassword()));

			usuarioService.cadastrar(usuario);
			
			JOptionPane.showMessageDialog(null,"Usuário cadastrado com sucesso!","Cadastro Bem-sucedido",JOptionPane.INFORMATION_MESSAGE);
			
			voltarParaLogin();

		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null,	"Data de nascimento inválida! Use o formato DD/MM/AAAA","Data Inválida",JOptionPane.ERROR_MESSAGE);
		} catch (UsuarioDuplicadoException e) {
			JOptionPane.showMessageDialog(null,"Nome de usuário já existe! Escolha outro.","Usuário Duplicado",JOptionPane.ERROR_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null,e.getMessage(),"Erro de Validação",JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Erro ao cadastrar usuário: " + e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean validarCampos() {
		if (txtNomeCompleto.getText().trim().isEmpty() ||
			txtDataNascimento.getText().trim().isEmpty() ||
			txtNomeUsuario.getText().trim().isEmpty() ||
			txtSenha.getPassword().length == 0 ||
			txtConfirmarSenha.getPassword().length == 0) {
			
			JOptionPane.showMessageDialog(null,"Por favor, preencha todos os campos!","Campos Obrigatórios",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean validarSenhas() {
		String senha = new String(txtSenha.getPassword());
		String confirmacao = new String(txtConfirmarSenha.getPassword());
		return senha.equals(confirmacao);
	}

	private void voltarParaLogin() {
		this.loginWindow.setVisible(true);
		this.dispose();
	}

	private void limparCampos() {
		txtNomeCompleto.setText("");
		txtDataNascimento.setText("");
		cbSexo.setSelectedIndex(0);
		txtNomeUsuario.setText("");
		txtSenha.setText("");
		txtConfirmarSenha.setText("");
	}

	public void inicializarComponentes() {
		setTitle("Cadastro de Usuário");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Cadastro de Usuário");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setBounds(150, 20, 200, 30);
		contentPane.add(lblTitulo);

		JLabel lblNomeCompleto = new JLabel("Nome Completo:");
		lblNomeCompleto.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNomeCompleto.setBounds(50, 70, 100, 20);
		contentPane.add(lblNomeCompleto);

		txtNomeCompleto = new JTextField();
		txtNomeCompleto.setBounds(150, 70, 250, 25);
		contentPane.add(txtNomeCompleto);
		txtNomeCompleto.setColumns(10);

		JLabel lblDataNascimento = new JLabel("Data Nascimento:");
		lblDataNascimento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDataNascimento.setBounds(50, 110, 100, 20);
		contentPane.add(lblDataNascimento);

		txtDataNascimento = new JTextField();
		txtDataNascimento.setBounds(150, 110, 100, 25);
		contentPane.add(txtDataNascimento);
		txtDataNascimento.setColumns(10);

		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSexo.setBounds(50, 150, 50, 20);
		contentPane.add(lblSexo);

		cbSexo = new JComboBox<>();
		cbSexo.setBounds(150, 150, 100, 25);
		cbSexo.addItem("M");
		cbSexo.addItem("F");
		contentPane.add(cbSexo);

		JLabel lblNomeUsuario = new JLabel("Nome Usuário:");
		lblNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNomeUsuario.setBounds(50, 190, 100, 20);
		contentPane.add(lblNomeUsuario);

		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setBounds(150, 190, 150, 25);
		contentPane.add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(50, 230, 50, 20);
		contentPane.add(lblSenha);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(150, 230, 150, 25);
		contentPane.add(txtSenha);

		JLabel lblConfirmarSenha = new JLabel("Confirmar Senha:");
		lblConfirmarSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblConfirmarSenha.setBounds(50, 270, 100, 20);
		contentPane.add(lblConfirmarSenha);

		txtConfirmarSenha = new JPasswordField();
		txtConfirmarSenha.setBounds(150, 270, 150, 25);
		contentPane.add(txtConfirmarSenha);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltarParaLogin();
			}
		});
		btnVoltar.setBounds(100, 320, 80, 30);
		contentPane.add(btnVoltar);

		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarUsuario();
			}
		});
		btnCadastrar.setBounds(300, 320, 100, 30);
		contentPane.add(btnCadastrar);
	}
}