package br.edu.utfpr.projetofinanceiro.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;



import javax.swing.*;
import java.awt.*;
import java.util.List;

import br.edu.utfpr.projetofinanceiro.entity.Usuario;

public class RelatorioWindow extends JFrame {
    private JPanel mainPanel;
    private TelaPrincipalWindow telaAnterior;
    private Usuario usuarioLogado;
    
    public RelatorioWindow(TelaPrincipalWindow telaAnterior, Usuario usuarioLogado) {
        this.telaAnterior = telaAnterior;
        this.usuarioLogado = usuarioLogado;
        
        initializeUI();
        
        
    }
    
    private void initializeUI() {
        setTitle("Sistema Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("RELATÓRIOS E INDICADORES - Usuário: " + usuarioLogado.getNomeUsuario()));
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel optionsPanel = createOptionsPanel();
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        
        JButton voltarButton = new JButton("Voltar para Tela Principal");
        voltarButton.addActionListener(e -> voltarParaPrincipal());
        mainPanel.add(voltarButton, BorderLayout.SOUTH);
        
        getContentPane().add(mainPanel);
    }
    
    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton btnRelatorioMensal = new JButton("Relatório Mensal");
        btnRelatorioMensal.setBounds(20, 20, 746, 153);
        btnRelatorioMensal.addActionListener(e -> abrirRelatorioMensal());
        
        JButton btnRelatorioAnual = new JButton("Relatório Anual");
        btnRelatorioAnual.setBounds(20, 183, 746, 153);
        btnRelatorioAnual.addActionListener(e -> abrirRelatorioAnual());
        
        JButton btnExportar = new JButton("Exportar Relatórios");
        btnExportar.setBounds(20, 346, 746, 153);
        btnExportar.addActionListener(e -> abrirExportacao());
        panel.setLayout(null);
        
        panel.add(btnRelatorioMensal);
        panel.add(btnRelatorioAnual);
        panel.add(btnExportar);
        
        return panel;
    }
    
    private void abrirRelatorioMensal() {
    	
    	RelatorioMensal relatorioMensal = new RelatorioMensal();
    	relatorioMensal.setVisible(true);
    	
        JOptionPane.showMessageDialog(this, "Abrindo Relatório Mensal...");
    }
    
    private void abrirRelatorioAnual() {
    	
    	RelatorioAnual relatorioAnual = new RelatorioAnual();
    	relatorioAnual.setVisible(true);
    	
        JOptionPane.showMessageDialog(this, "Abrindo Relatório Anual...");
    }
    
    private void abrirExportacao() {
        JOptionPane.showMessageDialog(this, "Abrindo Exportação...");
    }
    
    private void voltarParaPrincipal() {
        this.dispose();
        telaAnterior.setVisible(true);
    }
}