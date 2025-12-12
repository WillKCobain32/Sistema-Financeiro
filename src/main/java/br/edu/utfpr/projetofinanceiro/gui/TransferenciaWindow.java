package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
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

import br.edu.utfpr.projetofinanceiro.entity.Conta;
import br.edu.utfpr.projetofinanceiro.entity.Transferencia;
import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.exception.SaldoInsuficienteException;
import br.edu.utfpr.projetofinanceiro.exception.ValidacaoException;
import br.edu.utfpr.projetofinanceiro.service.TransferenciaService;

public class TransferenciaWindow extends JFrame {

    private JPanel contentPane;
    private JComboBox<Conta> comboContaOrigem;
    private JComboBox<Conta> comboContaDestino;
    private JTextField txtValor;
    private JTextField txtData;
    private JTable tblTransferencias;
    private DefaultTableModel tableModel;
    
    private TransferenciaService transferenciaService;
    private Usuario usuarioLogado;
    private JFrame telaAnterior;
    private List<Conta> contasUsuario;

    public TransferenciaWindow(JFrame telaAnterior, Usuario usuarioLogado) {
        this.telaAnterior = telaAnterior;
        this.usuarioLogado = usuarioLogado;
        this.transferenciaService = new TransferenciaService();
        this.carregarContasUsuario();
        this.inicializarComponentes();
        this.carregarTransferencias();
    }

    private void carregarContasUsuario() {
        try {
            this.contasUsuario = transferenciaService.buscarContasUsuario(usuarioLogado);
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar contas: " + e.getMessage());
            this.contasUsuario = Collections.emptyList();
        }
    }

    private void inicializarComponentes() {
        setTitle("Transferências entre Contas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Transferências entre Contas");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 300, 25);
        contentPane.add(lblTitulo);

        JLabel lblContaOrigem = new JLabel("Conta Origem:");
        lblContaOrigem.setBounds(20, 50, 100, 20);
        contentPane.add(lblContaOrigem);

        comboContaOrigem = new JComboBox<>();
        for (Conta conta : contasUsuario) {
            comboContaOrigem.addItem(conta);
        }
        comboContaOrigem.setBounds(120, 50, 300, 20);
        contentPane.add(comboContaOrigem);

        JLabel lblContaDestino = new JLabel("Conta Destino:");
        lblContaDestino.setBounds(20, 80, 100, 20);
        contentPane.add(lblContaDestino);

        comboContaDestino = new JComboBox<>();
        for (Conta conta : contasUsuario) {
            comboContaDestino.addItem(conta);
        }
        comboContaDestino.setBounds(120, 80, 300, 20);
        contentPane.add(comboContaDestino);

        JLabel lblValor = new JLabel("Valor:");
        lblValor.setBounds(20, 110, 100, 20);
        contentPane.add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(120, 110, 150, 20);
        contentPane.add(txtValor);

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(20, 140, 100, 20);
        contentPane.add(lblData);

        txtData = new JTextField();
        txtData.setBounds(120, 140, 150, 20);
        contentPane.add(txtData);

        JButton btnTransferir = new JButton("Realizar Transferência");
        btnTransferir.setBounds(20, 210, 180, 25);
        btnTransferir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarTransferencia();
            }
        });
        contentPane.add(btnTransferir);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(210, 210, 80, 25);
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirTransferencia();
            }
        });
        contentPane.add(btnExcluir);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(300, 210, 80, 25);
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        });
        contentPane.add(btnVoltar);

        String[] colunas = {"ID", "Conta Origem", "Conta Destino", "Valor", "Data"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTransferencias = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblTransferencias);
        scrollPane.setBounds(20, 250, 850, 300);
        contentPane.add(scrollPane);
    }

    private void realizarTransferencia() {
        try {
            if (comboContaOrigem.getSelectedItem() == null || comboContaDestino.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione conta origem e destino");
                return;
            }
            
            Conta contaOrigem = (Conta) comboContaOrigem.getSelectedItem();
            Conta contaDestino = (Conta) comboContaDestino.getSelectedItem();
            
            if (contaOrigem.getIdConta() == contaDestino.getIdConta()) {
                JOptionPane.showMessageDialog(this, "Conta origem e destino devem ser diferentes");
                return;
            }

            if (txtValor.getText().isEmpty() || txtData.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios");
                return;
            }

            Transferencia transferencia = new Transferencia();
            transferencia.setIdUsuario(usuarioLogado.getIdUsuario());
            transferencia.setIdContaOrigem(contaOrigem.getIdConta());
            transferencia.setIdContaDestino(contaDestino.getIdConta());
            transferencia.setValor(Double.parseDouble(txtValor.getText()));
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataUtil = sdf.parse(txtData.getText());
            transferencia.setData(new Date(dataUtil.getTime()));

            transferenciaService.realizarTransferencia(transferencia, usuarioLogado);
            
            JOptionPane.showMessageDialog(this, "Transferência realizada com sucesso!");
            limparCampos();
            carregarTransferencias();
            
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SaldoInsuficienteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Saldo Insuficiente", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor deve ser um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirTransferencia() {
        int linha = tblTransferencias.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma transferência para excluir");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Confirma a exclusão desta transferência?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idTransferencia = (int) tableModel.getValueAt(linha, 0);
                transferenciaService.excluirTransferencia(idTransferencia, usuarioLogado);
                JOptionPane.showMessageDialog(this, "Transferência excluída com sucesso!");
                carregarTransferencias();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTransferencias() {
        try {
            List<Transferencia> transferencias = transferenciaService.buscarTransferenciasUsuario(usuarioLogado.getIdUsuario());
            tableModel.setRowCount(0);
            
            for (Transferencia t : transferencias) {
                String nomeOrigem = transferenciaService.getNomeConta(t.getIdContaOrigem());
                String nomeDestino = transferenciaService.getNomeConta(t.getIdContaDestino());
                
                tableModel.addRow(new Object[]{
                    t.getIdTransferencia(),
                    nomeOrigem,
                    nomeDestino,
                    String.format("R$ %.2f", t.getValor()),
                    t.getData()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar transferências: " + e.getMessage());
        }
    }

    private void limparCampos() {
        if (comboContaOrigem.getItemCount() > 0) comboContaOrigem.setSelectedIndex(0);
        if (comboContaDestino.getItemCount() > 0) comboContaDestino.setSelectedIndex(0);
        txtValor.setText("");
    }

    private void voltar() {
        this.dispose();
        telaAnterior.setVisible(true);
    }
}