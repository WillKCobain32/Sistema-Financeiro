package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.SaldoInsuficienteException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.service.ContaService;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

public class ContaWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNomeBanco;
    private JTextField txtAgencia;
    private JTextField txtNumeroConta;
    private JTextField txtSaldoInicial;
    private JComboBox<String> cbTipoConta;
    private JButton btnCadastrar;
    private JButton btnLimpar;
    private JButton btnVoltar;

    private ContaService contaService;
    private Usuario usuarioLogado;
    private TelaPrincipalWindow telaPrincipalWindow;
    private JTable tblContas;
    private JTextField txtIDConta;

    public ContaWindow(TelaPrincipalWindow telaPrincipalWindow, Usuario usuarioLogado) {
        this.telaPrincipalWindow = telaPrincipalWindow;
        this.usuarioLogado = usuarioLogado;
        this.contaService = new ContaService();
        this.inicializarComponentes();
        this.buscarContas();
    }

    private void cadastrarConta() {
        try {
            if (!validarCampos()) {
                return;
            }

            Conta conta = new Conta();
            conta.setIdUsuario(usuarioLogado.getIdUsuario());
            conta.setNomeBanco(txtNomeBanco.getText().trim());
            conta.setAgencia(txtAgencia.getText().trim());
            conta.setNumeroConta(txtNumeroConta.getText().trim());
            conta.setSaldoInicial(Double.parseDouble(txtSaldoInicial.getText().trim()));
            conta.setTipoConta(cbTipoConta.getSelectedItem().toString());

            int resultado = contaService.cadastrar(conta);
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null,"Conta cadastrada com sucesso!", "Cadastro Bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                buscarContas();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Saldo inicial deve ser um valor numérico válido!","Erro de Formato",JOptionPane.ERROR_MESSAGE);
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Erro de Validação",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(null,"Erro ao cadastrar conta: " + e.getMessage(),"Erro de Banco de Dados",JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (txtNomeBanco.getText().trim().isEmpty() ||
            txtAgencia.getText().trim().isEmpty() ||
            txtNumeroConta.getText().trim().isEmpty() ||
            txtSaldoInicial.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "Campos Obrigatórios",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limparCampos() {
        txtNomeBanco.setText("");
        txtAgencia.setText("");
        txtNumeroConta.setText("");
        txtSaldoInicial.setText("");
        cbTipoConta.setSelectedIndex(0);
    }

    private void voltarTelaPrincipal() {
        this.telaPrincipalWindow.setVisible(true);
        this.dispose();
    }
    
    private void buscarContas() {
    	try {
    		List<Conta> contas;
    		contas = contaService.buscarContasPorUsuario(usuarioLogado.getIdUsuario());
    		DefaultTableModel modelo = (DefaultTableModel) tblContas.getModel();
    		modelo.fireTableDataChanged();
    		modelo.setRowCount(0);
    		for (Conta conta : contas) {
				modelo.addRow(new Object[] {
						conta.getIdConta(),conta.getNomeBanco(),conta.getAgencia(),conta.getNumeroConta(),conta.getSaldoInicial(),conta.getTipoConta()
				});
			}
    		
    		
    	}catch(SQLException | IOException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Contas",JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    private void excluirConta() {
    	try {
	    	int resposta = JOptionPane.showConfirmDialog(null,"Tem certeza que deseja excluir esta conta?", "Confirmação de Exclusão",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
	    	
	    	if (resposta != JOptionPane.YES_OPTION) {
	            return;
	        }
	    	
	    	int resultado = contaService.excluir(Integer.parseInt(txtIDConta.getText().trim()));
	    	buscarContas();
	    	
	    	
	    	if(resultado >0) {
	    		JOptionPane.showMessageDialog(null, "Conta Excluida com sucesso","Exclusão",JOptionPane.INFORMATION_MESSAGE);
	    	}else {
	    		JOptionPane.showMessageDialog(null, "Conta não existente","Exclusão",JOptionPane.WARNING_MESSAGE);
	    	}
	    	    	
    	}catch(SQLException | IOException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Contas",JOptionPane.ERROR_MESSAGE);
    	}
    	
    	
    }
    
    private void atualizarConta() {
    	try {
            if (!validarCampos()) {
                return;
            }

            Conta conta = new Conta();
            conta.setIdConta(Integer.parseInt(txtIDConta.getText().trim()));
            conta.setIdUsuario(usuarioLogado.getIdUsuario());
            conta.setNomeBanco(txtNomeBanco.getText().trim());
            conta.setAgencia(txtAgencia.getText().trim());
            conta.setNumeroConta(txtNumeroConta.getText().trim());
            conta.setSaldoInicial(Double.parseDouble(txtSaldoInicial.getText().trim()));
            conta.setTipoConta(cbTipoConta.getSelectedItem().toString());

            int resultado;

				resultado = contaService.atualizar(conta);
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null,"Conta atualizada com sucesso!", "Atualização Bem-sucedida", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                buscarContas();
            }else
            	JOptionPane.showMessageDialog(null, "Nenhuma Atualização realizada","Atualização",JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Saldo inicial deve ser um valor numérico válido!","Erro de Formato",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar conta: " + e.getMessage(),"Erro de Banco de Dados",JOptionPane.ERROR_MESSAGE);
        } catch(IOException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Contas",JOptionPane.ERROR_MESSAGE);
    	}
    }
    

    public void inicializarComponentes() {
        setTitle("Cadastro de Contas Bancárias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 690, 540);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        JLabel lblTitulo = new JLabel("Cadastro de Conta Bancária");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(202, 11, 250, 30);
        contentPane.add(lblTitulo);

     
        JLabel lblNomeBanco = new JLabel("Nome do Banco:");
        lblNomeBanco.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNomeBanco.setBounds(33, 44, 100, 20);
        contentPane.add(lblNomeBanco);

        txtNomeBanco = new JTextField();
        txtNomeBanco.setBounds(148, 43, 250, 25);
        contentPane.add(txtNomeBanco);
        txtNomeBanco.setColumns(10);

       
        JLabel lblAgencia = new JLabel("Agência:");
        lblAgencia.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblAgencia.setBounds(33, 75, 100, 20);
        contentPane.add(lblAgencia);

        txtAgencia = new JTextField();
        txtAgencia.setBounds(148, 74, 150, 25);
        contentPane.add(txtAgencia);
        txtAgencia.setColumns(10);

        
        JLabel lblNumeroConta = new JLabel("Número da Conta:");
        lblNumeroConta.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNumeroConta.setBounds(33, 106, 100, 20);
        contentPane.add(lblNumeroConta);

        txtNumeroConta = new JTextField();
        txtNumeroConta.setBounds(148, 105, 150, 25);
        contentPane.add(txtNumeroConta);
        txtNumeroConta.setColumns(10);

      
        JLabel lblTipoConta = new JLabel("Tipo de Conta:");
        lblTipoConta.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblTipoConta.setBounds(33, 137, 100, 20);
        contentPane.add(lblTipoConta);

        cbTipoConta = new JComboBox<>();
        cbTipoConta.setBounds(148, 136, 150, 25);
        cbTipoConta.addItem("Conta Corrente");
        cbTipoConta.addItem("Conta Poupança");
        cbTipoConta.addItem("Conta Salário");
        cbTipoConta.addItem("Conta Digital");
        contentPane.add(cbTipoConta);

      
        JLabel lblSaldoInicial = new JLabel("Saldo Inicial:");
        lblSaldoInicial.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblSaldoInicial.setBounds(33, 168, 100, 20);
        contentPane.add(lblSaldoInicial);

        txtSaldoInicial = new JTextField();
        txtSaldoInicial.setBounds(148, 167, 150, 25);
        contentPane.add(txtSaldoInicial);
        txtSaldoInicial.setColumns(10);

       
        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	voltarTelaPrincipal();
            }
        });
        btnVoltar.setBounds(50, 290, 80, 30);
        contentPane.add(btnVoltar);

      
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        btnLimpar.setBounds(150, 290, 80, 30);
        contentPane.add(btnLimpar);

     
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarConta();
            }
        });
        btnCadastrar.setBounds(332, 294, 100, 30);
        contentPane.add(btnCadastrar);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Contas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(33, 331, 619, 159);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(26, 27, 560, 107);
        panel.add(scrollPane);
        
        tblContas = new JTable();
        scrollPane.setViewportView(tblContas);
        tblContas.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"ID Conta", "Nome Do banco", "Agência", "Numero da Conta", "Saldo Inicial", "Tipo Conta"
        	}
        ));
        
        JLabel lblInformacao = new JLabel("Excluir ou Editar");
        lblInformacao.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblInformacao.setBounds(33, 213, 119, 14);
        contentPane.add(lblInformacao);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		atualizarConta();
        	}
        });
        btnAtualizar.setBounds(442, 294, 100, 30);
        contentPane.add(btnAtualizar);
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		excluirConta();
        	}
        });
        btnExcluir.setBounds(552, 294, 100, 30);
        contentPane.add(btnExcluir);
        
        JLabel lblIDConta = new JLabel("ID Conta:");
        lblIDConta.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblIDConta.setBounds(33, 238, 58, 20);
        contentPane.add(lblIDConta);
        
        txtIDConta = new JTextField();
        txtIDConta.setBounds(101, 238, 86, 20);
        contentPane.add(txtIDConta);
        txtIDConta.setColumns(10);
    }
}