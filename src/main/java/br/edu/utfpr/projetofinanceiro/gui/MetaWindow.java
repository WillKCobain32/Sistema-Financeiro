package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

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
import javax.swing.table.DefaultTableModel;

import br.edu.utfpr.projetofinanceiro.entity.Meta;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.service.MetaService;

public class MetaWindow extends JFrame {

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtValorTotal;
    private JTextField txtValorMensal;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JComboBox<String> comboTipo;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private MetaService metaService;
    private Usuario usuarioLogado;
    private JFrame telaAnterior;
    
    private Meta metaSelecionada;

    public MetaWindow(JFrame telaAnterior, Usuario usuarioLogado) {
        this.telaAnterior = telaAnterior;
        this.usuarioLogado = usuarioLogado;
        this.metaService = new MetaService();
        this.inicializarComponentes();
        this.carregarMetas();
    }

    private void inicializarComponentes() {
        setTitle("Gerenciar Metas Financeiras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Metas Financeiras");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 200, 25);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 50, 100, 20);
        contentPane.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(120, 50, 200, 20);
        contentPane.add(txtNome);
        txtNome.setColumns(10);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 80, 100, 20);
        contentPane.add(lblTipo);

        comboTipo = new JComboBox<>(new String[]{"LONGO_PRAZO", "FUNDO_OCASIONAL"});
        comboTipo.setBounds(120, 80, 200, 20);
        contentPane.add(comboTipo);

        JLabel lblValorTotal = new JLabel("Valor Total:");
        lblValorTotal.setBounds(20, 110, 100, 20);
        contentPane.add(lblValorTotal);

        txtValorTotal = new JTextField();
        txtValorTotal.setBounds(120, 110, 200, 20);
        contentPane.add(txtValorTotal);

        JLabel lblValorMensal = new JLabel("Valor Mensal:");
        lblValorMensal.setBounds(20, 140, 100, 20);
        contentPane.add(lblValorMensal);

        txtValorMensal = new JTextField();
        txtValorMensal.setBounds(120, 140, 200, 20);
        contentPane.add(txtValorMensal);

        JLabel lblDataInicio = new JLabel("Data Início:");
        lblDataInicio.setBounds(20, 170, 100, 20);
        contentPane.add(lblDataInicio);

        txtDataInicio = new JTextField();
        txtDataInicio.setBounds(120, 170, 200, 20);
        contentPane.add(txtDataInicio);

        JLabel lblDataFim = new JLabel("Data Fim:");
        lblDataFim.setBounds(20, 200, 100, 20);
        contentPane.add(lblDataFim);

        txtDataFim = new JTextField();
        txtDataFim.setBounds(120, 200, 200, 20);
        contentPane.add(txtDataFim);

        JButton btnNovo = new JButton("Nova Meta");
        btnNovo.setBounds(20, 240, 100, 25);
        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                novaMeta();
            }
        });
        contentPane.add(btnNovo);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(130, 240, 80, 25);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarMeta();
            }
        });
        contentPane.add(btnSalvar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(220, 240, 80, 25);
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarMeta();
            }
        });
        contentPane.add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(310, 240, 80, 25);
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirMeta();
            }
        });
        contentPane.add(btnExcluir);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(400, 240, 80, 25);
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        });
        contentPane.add(btnVoltar);
        String[] colunas = {"ID", "Nome", "Tipo", "Valor Total", "Valor Mensal", "Data Início", "Data Fim"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 280, 850, 250);
        contentPane.add(scrollPane);
    }

    private void novaMeta() {
        limparCampos();
        metaSelecionada = null;
    }

    private void salvarMeta() {
        try {
            if (txtValorTotal.getText().isEmpty() || txtValorMensal.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Valor Total e Valor Mensal são obrigatórios", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Meta meta = new Meta();
            meta.setIdUsuario(usuarioLogado.getIdUsuario());
            meta.setNome(txtNome.getText());
            meta.setTipo(comboTipo.getSelectedItem().toString());
            meta.setValorTotal(Double.parseDouble(txtValorTotal.getText()));
            meta.setValorMensal(Double.parseDouble(txtValorMensal.getText()));
            meta.setDataInicio(txtDataInicio.getText());
            meta.setDataFim(txtDataFim.getText());

            if (metaSelecionada == null) {
                metaService.cadastrar(meta);
                JOptionPane.showMessageDialog(this, "Meta cadastrada com sucesso!");
            } else {
                meta.setIdMeta(metaSelecionada.getIdMeta());
                metaService.atualizar(meta);
                JOptionPane.showMessageDialog(this, "Meta atualizada com sucesso!");
            }
            
            carregarMetas();
            limparCampos();
            
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor Total e Valor Mensal devem ser números válidos", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarMeta() {
        int linha = table.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma meta para editar");
            return;
        }
        
        try {
            int idMeta = (int) tableModel.getValueAt(linha, 0);
            metaSelecionada = metaService.buscarPorChave(idMeta);
            if (metaSelecionada != null) {
                preencherCampos(metaSelecionada);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar meta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirMeta() {
        int linha = table.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma meta para excluir");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Confirma a exclusão desta meta?", 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idMeta = (int) tableModel.getValueAt(linha, 0);
                metaService.excluir(idMeta);
                JOptionPane.showMessageDialog(this, "Meta excluída com sucesso!");
                carregarMetas();
                limparCampos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarMetas() {
        try {
            List<Meta> metas = metaService.buscarTodos();
            tableModel.setRowCount(0);
            
            for (Meta meta : metas) {
                if (meta.getIdUsuario() == usuarioLogado.getIdUsuario()) {
                    tableModel.addRow(new Object[]{
                        meta.getIdMeta(),
                        meta.getNome(),
                        meta.getTipo(),
                        String.format("R$ %.2f", meta.getValorTotal()),
                        String.format("R$ %.2f", meta.getValorMensal()),
                        meta.getDataInicio(),
                        meta.getDataFim()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar metas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos(Meta meta) {
        txtNome.setText(meta.getNome());
        comboTipo.setSelectedItem(meta.getTipo());
        txtValorTotal.setText(String.valueOf(meta.getValorTotal()));
        txtValorMensal.setText(String.valueOf(meta.getValorMensal()));
        txtDataInicio.setText(meta.getDataInicio());
        txtDataFim.setText(meta.getDataFim());
    }

    private void limparCampos() {
        txtNome.setText("");
        comboTipo.setSelectedIndex(0);
        txtValorTotal.setText("");
        txtValorMensal.setText("");
        txtDataInicio.setText("");
        txtDataFim.setText("");
        metaSelecionada = null;
    }

    private void voltar() {
        this.dispose();
        telaAnterior.setVisible(true);
    }
}