package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import br.edu.utfpr.projetofinanceiro.entity.Categoria;
import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Lancamento;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.service.CategoriaService;
import br.edu.utfpr.projetofinanceiro.service.ContaService;
import br.edu.utfpr.projetofinanceiro.service.LancamentoService;

public class LancamentoWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JTextField txtDataLancamento;
    private JComboBox<Conta> cbConta;
    private JComboBox<Categoria> cbCategoria;
    private JComboBox<String> cbFixo;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnExcluir;
    private JButton btnVoltar;
    private JTable tblLancamentos;
    
    private LancamentoService lancamentoService;
    private ContaService contaService;
    private CategoriaService categoriaService;
    private Usuario usuarioLogado;
    private TelaPrincipalWindow dashboardWindow;
    private DefaultTableModel tableModel;
    private Lancamento lancamentoEditando;

    public LancamentoWindow(TelaPrincipalWindow dashboardWindow, Usuario usuarioLogado) {
        this.dashboardWindow = dashboardWindow;
        this.usuarioLogado = usuarioLogado;
        this.lancamentoService = new LancamentoService();
        this.contaService = new ContaService();
        this.categoriaService = new CategoriaService();
        this.lancamentoEditando = null;
        this.inicializarComponentes();
        this.carregarCombos();
        this.carregarLancamentos();
    }

    private void carregarCombos() {
        try {
            // Carregar contas do usuário
            List<Conta> contas = contaService.buscarContasPorUsuario(usuarioLogado.getIdUsuario());
            cbConta.removeAllItems();
            for (Conta conta : contas) {
                cbConta.addItem(conta);
            }
            
            // Carregar categorias
            List<Categoria> categorias = categoriaService.buscarTodos();
            cbCategoria.removeAllItems();
            for (Categoria categoria : categorias) {
                cbCategoria.addItem(categoria);
            }
            
            // Verificar se tem dados
            if (contas.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Cadastre pelo menos uma conta antes de criar lançamentos!", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
            if (categorias.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Cadastre pelo menos uma categoria antes de criar lançamentos!", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar dados: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarLancamentos() {
        try {
            List<Lancamento> lancamentos = lancamentoService.buscarPorUsuario(usuarioLogado.getIdUsuario());
            tableModel.setRowCount(0);
            
            for (Lancamento lancamento : lancamentos) {
                // Buscar nome da conta e categoria
                String nomeConta = "N/A";
                String nomeCategoria = "N/A";
                String tipo = "N/A";
                
                for (int i = 0; i < cbConta.getItemCount(); i++) {
                    Conta conta = cbConta.getItemAt(i);
                    if (conta.getIdConta() == lancamento.getIdConta()) {
                        nomeConta = conta.getNomeBanco();
                        break;
                    }
                }
                
                for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                    Categoria categoria = cbCategoria.getItemAt(i);
                    if (categoria.getIdCategoria() == lancamento.getIdCategoria()) {
                        nomeCategoria = categoria.getNome();
                        tipo = categoria.getTipo();
                        break;
                    }
                }
                
                tableModel.addRow(new Object[] {
                    lancamento.getIdLancamento(),
                    nomeConta,
                    nomeCategoria,
                    tipo,
                    lancamento.getDescricao(),
                    String.format("R$ %.2f", lancamento.getValor()),
                    lancamento.getDataLancamento(),
                    lancamento.getFixo()
                });
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar lançamentos: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novoLancamento() {
        if (cbConta.getItemCount() == 0 || cbCategoria.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "É necessário ter contas e categorias cadastradas antes de criar lançamentos!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        this.lancamentoEditando = null;
        limparCampos();
        habilitarFormulario(true);
    }

    private void editarLancamento() {
        int selectedRow = tblLancamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um lançamento para editar!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idLancamento = (int) tableModel.getValueAt(selectedRow, 0);
            this.lancamentoEditando = lancamentoService.buscarPorChave(idLancamento);
            
            if (lancamentoEditando != null) {
                preencherFormulario(lancamentoEditando);
                habilitarFormulario(true);
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar lançamento: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarLancamento() {
        try {
            if (!validarCampos()) {
                return;
            }

            Lancamento lancamento;
            if (lancamentoEditando == null) {
                lancamento = new Lancamento(); 
            } else {
                lancamento = lancamentoEditando;
            }
            
            lancamento.setIdUsuario(usuarioLogado.getIdUsuario());
            lancamento.setIdConta(((Conta) cbConta.getSelectedItem()).getIdConta());
            lancamento.setIdCategoria(((Categoria) cbCategoria.getSelectedItem()).getIdCategoria());
            lancamento.setDescricao(txtDescricao.getText().trim());
            lancamento.setValor(Double.parseDouble(txtValor.getText().trim()));
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataUtil = sdf.parse(txtDataLancamento.getText());
            lancamento.setDataLancamento(new Date(dataUtil.getTime()));
            
            lancamento.setFixo(cbFixo.getSelectedItem().toString());

            int resultado;
            if (lancamentoEditando == null) {
                resultado = lancamentoService.cadastrar(lancamento);
            } else {
                resultado = lancamentoService.atualizar(lancamento);
            }
            
            if (resultado > 0) {
                String mensagem = lancamentoEditando == null ? "cadastrado" : "atualizado";
                JOptionPane.showMessageDialog(this, 
                    "Lançamento " + mensagem + " com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                carregarLancamentos();
                limparCampos();
                habilitarFormulario(false);
                this.lancamentoEditando = null;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Valor deve ser um número válido!", 
                "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Data inválida! Use o formato DD/MM/AAAA", 
                "Erro de Data", 
                JOptionPane.ERROR_MESSAGE);
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Erro de Validação", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar lançamento: " + e.getMessage(), 
                "Erro de Banco de Dados", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void excluirLancamento() {
        int selectedRow = tblLancamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um lançamento para excluir!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir este lançamento?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idLancamento = (int) tableModel.getValueAt(selectedRow, 0);
                int resultado = lancamentoService.excluir(idLancamento);
                
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Lançamento excluído com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                    carregarLancamentos();
                }
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir lançamento: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void preencherFormulario(Lancamento lancamento) {
        txtDescricao.setText(lancamento.getDescricao());
        txtValor.setText(String.valueOf(lancamento.getValor()));
        
        // Formatar data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDataLancamento.setText(sdf.format(lancamento.getDataLancamento()));
        
        // Selecionar conta
        for (int i = 0; i < cbConta.getItemCount(); i++) {
            Conta conta = cbConta.getItemAt(i);
            if (conta.getIdConta() == lancamento.getIdConta()) {
                cbConta.setSelectedIndex(i);
                break;
            }
        }
        
        // Selecionar categoria
        for (int i = 0; i < cbCategoria.getItemCount(); i++) {
            Categoria categoria = cbCategoria.getItemAt(i);
            if (categoria.getIdCategoria() == lancamento.getIdCategoria()) {
                cbCategoria.setSelectedIndex(i);
                break;
            }
        }
        
        cbFixo.setSelectedItem(lancamento.getFixo());
    }

    private void limparCampos() {
        txtDescricao.setText("");
        txtValor.setText("");
        txtDataLancamento.setText("");
        cbConta.setSelectedIndex(0);
        cbCategoria.setSelectedIndex(0);
        cbFixo.setSelectedIndex(0);
    }

    private void habilitarFormulario(boolean habilitar) {
        txtDescricao.setEnabled(habilitar);
        txtValor.setEnabled(habilitar);
        txtDataLancamento.setEnabled(habilitar);
        cbConta.setEnabled(habilitar);
        cbCategoria.setEnabled(habilitar);
        cbFixo.setEnabled(habilitar);
        btnSalvar.setEnabled(habilitar);
        btnCancelar.setEnabled(habilitar);
    }

    private boolean validarCampos() {
        if (txtDescricao.getText().trim().isEmpty() ||
            txtValor.getText().trim().isEmpty() ||
            txtDataLancamento.getText().trim().isEmpty() ||
            cbConta.getSelectedItem() == null ||
            cbCategoria.getSelectedItem() == null) {
            
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos!", 
                "Campos Obrigatórios", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void cancelarEdicao() {
        limparCampos();
        habilitarFormulario(false);
        this.lancamentoEditando = null;
    }

    private void voltarParaDashboard() {
        this.dashboardWindow.setVisible(true);
        this.dispose();
    }

    public void inicializarComponentes() {
        setTitle("Gerenciar Lançamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Título
        JLabel lblTitulo = new JLabel("Gerenciar Lançamentos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(320, 10, 250, 30);
        contentPane.add(lblTitulo);

        // Painel do Formulário
        JPanel panelForm = new JPanel();
        panelForm.setBorder(new TitledBorder("Dados do Lançamento"));
        panelForm.setBounds(20, 50, 850, 150);
        panelForm.setLayout(null);
        contentPane.add(panelForm);

        // Descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDescricao.setBounds(20, 30, 80, 20);
        panelForm.add(lblDescricao);

        txtDescricao = new JTextField();
        txtDescricao.setBounds(100, 30, 300, 25);
        panelForm.add(txtDescricao);
        txtDescricao.setColumns(10);

        // Valor
        JLabel lblValor = new JLabel("Valor:");
        lblValor.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblValor.setBounds(20, 65, 80, 20);
        panelForm.add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(100, 65, 150, 25);
        panelForm.add(txtValor);
        txtValor.setColumns(10);

        // Data
        JLabel lblData = new JLabel("Data:");
        lblData.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblData.setBounds(20, 100, 80, 20);
        panelForm.add(lblData);

        txtDataLancamento = new JTextField();
        txtDataLancamento.setBounds(100, 100, 150, 25);
        panelForm.add(txtDataLancamento);
        txtDataLancamento.setColumns(10);

        // Conta
        JLabel lblConta = new JLabel("Conta:");
        lblConta.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblConta.setBounds(300, 65, 80, 20);
        panelForm.add(lblConta);

        cbConta = new JComboBox<>();
        cbConta.setBounds(350, 65, 200, 25);
        panelForm.add(cbConta);

        // Categoria
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblCategoria.setBounds(300, 100, 80, 20);
        panelForm.add(lblCategoria);

        cbCategoria = new JComboBox<>();
        cbCategoria.setBounds(350, 100, 200, 25);
        panelForm.add(cbCategoria);

        // Fixo
        JLabel lblFixo = new JLabel("Fixo:");
        lblFixo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblFixo.setBounds(600, 30, 80, 20);
        panelForm.add(lblFixo);

        cbFixo = new JComboBox<>();
        cbFixo.setBounds(640, 30, 100, 25);
        cbFixo.addItem("S");
        cbFixo.addItem("N");
        panelForm.add(cbFixo);

        // Painel da Lista
        JPanel panelLista = new JPanel();
        panelLista.setBorder(new TitledBorder("Lançamentos Cadastrados"));
        panelLista.setBounds(20, 220, 850, 250);
        panelLista.setLayout(null);
        contentPane.add(panelLista);

        // Tabela de Lançamentos
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 20, 830, 220);
        panelLista.add(scrollPane);

        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Conta", "Categoria", "Tipo", "Descrição", "Valor", "Data", "Fixo"}
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLancamentos = new JTable(tableModel);
        scrollPane.setViewportView(tblLancamentos);

        // Painel de Botões
        JPanel panelBotoes = new JPanel();
        panelBotoes.setBounds(20, 480, 850, 50);
        panelBotoes.setLayout(null);
        contentPane.add(panelBotoes);

        // Botão Novo
        btnNovo = new JButton("Novo Lançamento");
        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                novoLancamento();
            }
        });
        btnNovo.setBounds(10, 10, 140, 30);
        panelBotoes.add(btnNovo);

        // Botão Editar
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarLancamento();
            }
        });
        btnEditar.setBounds(160, 10, 80, 30);
        panelBotoes.add(btnEditar);

        // Botão Excluir
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirLancamento();
            }
        });
        btnExcluir.setBounds(250, 10, 80, 30);
        panelBotoes.add(btnExcluir);

        // Botão Salvar
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarLancamento();
            }
        });
        btnSalvar.setBounds(340, 10, 80, 30);
        btnSalvar.setEnabled(false);
        panelBotoes.add(btnSalvar);

        // Botão Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarEdicao();
            }
        });
        btnCancelar.setBounds(430, 10, 90, 30);
        btnCancelar.setEnabled(false);
        panelBotoes.add(btnCancelar);

        // Botão Voltar
        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltarParaDashboard();
            }
        });
        btnVoltar.setBounds(750, 10, 80, 30);
        panelBotoes.add(btnVoltar);

        // Inicia com formulário desabilitado
        habilitarFormulario(false);
    }
}