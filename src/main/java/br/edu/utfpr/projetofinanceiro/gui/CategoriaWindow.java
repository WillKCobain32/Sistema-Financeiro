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

import br.edu.utfpr.projetofinanceiro.entity.Categoria;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.SaldoInsuficienteException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.service.CategoriaService;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

public class CategoriaWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNomeCategoria;
    private JComboBox<String> cbTipoCategoria;
    private JButton btnCadastrar;
    private JButton btnLimpar;
    private JButton btnVoltar;

    private CategoriaService categoriaService;
    private Usuario usuarioLogado;
    private TelaPrincipalWindow telaPrincipalWindow;
    private JTable tblCategorias;
    private JTextField txtIDCategoria;

    public CategoriaWindow(TelaPrincipalWindow telaPrincipalWindow, Usuario usuarioLogado) {
        this.telaPrincipalWindow = telaPrincipalWindow;
        this.usuarioLogado = usuarioLogado;
        this.categoriaService = new CategoriaService();
        this.inicializarComponentes();
        this.buscarCategorias();
    }

    private void cadastrarCategoria() {
        try {
            if (!validarCampos()) {
                return;
            }

            Categoria categoria = new Categoria();
        
            categoria.setNome(txtNomeCategoria.getText().trim());
            categoria.setTipo(cbTipoCategoria.getSelectedItem().toString());

            int resultado = categoriaService.cadastrar(categoria);
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null,"Categoria cadastrada com sucesso!", "Cadastro Bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                buscarCategorias();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Saldo inicial deve ser um valor numérico válido!","Erro de Formato",JOptionPane.ERROR_MESSAGE);
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Erro de Validação",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(null,"Erro ao cadastrar categoria: " + e.getMessage(),"Erro de Banco de Dados",JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (txtNomeCategoria.getText().trim().isEmpty()) {            
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "Campos Obrigatórios",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limparCampos() {
        txtNomeCategoria.setText("");
        cbTipoCategoria.setSelectedIndex(0);
    }

    private void voltarTelaPrincipal() {
        this.telaPrincipalWindow.setVisible(true);
        this.dispose();
    }
    
    private void buscarCategorias() {
    	try {
    		List<Categoria> categorias;
    		categorias = categoriaService.buscarTodos();
    		DefaultTableModel modelo = (DefaultTableModel) tblCategorias.getModel();
    		modelo.fireTableDataChanged();
    		modelo.setRowCount(0);
    		for (Categoria categoria : categorias) {
				modelo.addRow(new Object[] {
						categoria.getIdCategoria(),categoria.getNome(),categoria.getTipo()
				});
			}
    		
    		
    	}catch(SQLException | IOException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Categorias",JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    private void excluirCategoria() {
    	try {
	    	int resposta = JOptionPane.showConfirmDialog(null,"Tem certeza que deseja excluir esta categoria?", "Confirmação de Exclusão",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
	    	
	    	if (resposta != JOptionPane.YES_OPTION) {
	            return;
	        }
	    	
	    	int resultado = categoriaService.excluir(Integer.parseInt(txtIDCategoria.getText().trim()));
	    	buscarCategorias();
	    	
	    	
	    	if(resultado >0) {
	    		JOptionPane.showMessageDialog(null, "Categoria Excluida com sucesso","Exclusão",JOptionPane.INFORMATION_MESSAGE);
	    	}else {
	    		JOptionPane.showMessageDialog(null, "Categoria não existente","Exclusão",JOptionPane.WARNING_MESSAGE);
	    	}
	    	    	
    	}catch(SQLException | IOException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Categorias",JOptionPane.ERROR_MESSAGE);
    	}
    	
    	
    }
    
    private void atualizarCategoria() {
    	try {
            if (!validarCampos()) {
                return;
            }
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(Integer.parseInt(txtIDCategoria.getText().trim()));
            categoria.setNome(txtNomeCategoria.getText().trim());
            categoria.setTipo(cbTipoCategoria.getSelectedItem().toString());

            int resultado = categoriaService.atualizar(categoria);
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null,"Categoria atualizada com sucesso!", "Atualização Bem-sucedida", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                buscarCategorias();
            }else {
            	JOptionPane.showMessageDialog(null, "Nenhuma Atualização realizada","Atualização",JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Saldo inicial deve ser um valor numérico válido!","Erro de Formato",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar categoria: " + e.getMessage(),"Erro de Banco de Dados",JOptionPane.ERROR_MESSAGE);
        } catch(IOException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Categorias",JOptionPane.ERROR_MESSAGE);
    	}
    }
    

    public void inicializarComponentes() {
        setTitle("Cadastro de Categorias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 572, 450);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        JLabel lblTitulo = new JLabel("Cadastro de Categoria");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(80, 11, 250, 30);
        contentPane.add(lblTitulo);

     
        JLabel lblNomeCategoria = new JLabel("Nome Categoria:");
        lblNomeCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNomeCategoria.setBounds(33, 44, 100, 20);
        contentPane.add(lblNomeCategoria);

        txtNomeCategoria = new JTextField();
        txtNomeCategoria.setBounds(148, 43, 250, 25);
        contentPane.add(txtNomeCategoria);
        txtNomeCategoria.setColumns(10);

      
        JLabel lblTipoCategoria = new JLabel("Tipo Categoria:");
        lblTipoCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblTipoCategoria.setBounds(33, 75, 100, 20);
        contentPane.add(lblTipoCategoria);

        cbTipoCategoria = new JComboBox<>();
        cbTipoCategoria.setBounds(148, 74, 150, 25);
        cbTipoCategoria.addItem("RECEITA");
        cbTipoCategoria.addItem("DESPESA");
        contentPane.add(cbTipoCategoria);

       
        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	voltarTelaPrincipal();
            }
        });
        btnVoltar.setBounds(33, 191, 80, 30);
        contentPane.add(btnVoltar);

      
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        btnLimpar.setBounds(118, 191, 80, 30);
        contentPane.add(btnLimpar);

     
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarCategoria();
            }
        });
        btnCadastrar.setBounds(208, 191, 100, 30);
        contentPane.add(btnCadastrar);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Categorias", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(33, 232, 492, 162);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(31, 28, 429, 110);
        panel.add(scrollPane);
        
        tblCategorias = new JTable();
        scrollPane.setViewportView(tblCategorias);
        tblCategorias.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"ID Categoria", "Nome Categoria", "Tipo"
        	}
        ));
        
        JLabel lblInformacao = new JLabel("Excluir ou Editar");
        lblInformacao.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblInformacao.setBounds(33, 123, 119, 14);
        contentPane.add(lblInformacao);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		atualizarCategoria();
        	}
        });
        btnAtualizar.setBounds(318, 191, 100, 30);
        contentPane.add(btnAtualizar);
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		excluirCategoria();
        	}
        });
        btnExcluir.setBounds(434, 191, 100, 30);
        contentPane.add(btnExcluir);
        
        JLabel lblIDCategoria = new JLabel("ID Categoria:");
        lblIDCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblIDCategoria.setBounds(33, 148, 80, 20);
        contentPane.add(lblIDCategoria);
        
        txtIDCategoria = new JTextField();
        txtIDCategoria.setBounds(112, 148, 86, 20);
        contentPane.add(txtIDCategoria);
        txtIDCategoria.setColumns(10);
    }
}