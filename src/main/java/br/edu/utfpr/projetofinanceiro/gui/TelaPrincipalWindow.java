package br.edu.utfpr.projetofinanceiro.gui;


import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.service.ResumoFinanceiroService;

public class TelaPrincipalWindow extends JFrame {


	private JPanel contentPane;
	
	private Usuario usuarioLogado;
	
	private ResumoFinanceiroService resumoService;
	private JLabel lblSaldoTotal;
    private JLabel lblReceitasMes;
    private JLabel lblDespesasMes;
    private JLabel lblSaldoMes;


	
	public TelaPrincipalWindow(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
		this.inicializarComponentes();
		this.resumoService = new ResumoFinanceiroService();
		this.carregarResumoFinanceiro();
		
	}
	
	private void carregarResumoFinanceiro() {
        try {
            double saldoTotal = resumoService.getSaldoTotal(usuarioLogado);
            double receitasMes = resumoService.getReceitasMes(usuarioLogado);
            double despesasMes = resumoService.getDespesasMes(usuarioLogado);
            double saldoMes = resumoService.getSaldoMes(usuarioLogado);
            
            
            lblSaldoTotal.setText(String.format("Saldo Total: R$ %.2f", saldoTotal));
            lblReceitasMes.setText(String.format("Receitas do Mês: R$ %.2f", receitasMes));
            lblDespesasMes.setText(String.format("Despesas do Mês: R$ %.2f", despesasMes));
            lblSaldoMes.setText(String.format("Saldo do Mês: R$ %.2f", saldoMes));
            
           
            if (saldoMes < 0) {
                lblSaldoMes.setForeground(Color.RED);
            } else {
                lblSaldoMes.setForeground(new Color(0, 128, 0)); // Verde
            }
            
        } catch (Exception e) {
            lblSaldoTotal.setText("Saldo Total: R$ 0,00");
            lblReceitasMes.setText("Receitas do Mês: R$ 0,00");
            lblDespesasMes.setText("Despesas do Mês: R$ 0,00");
            lblSaldoMes.setText("Saldo do Mês: R$ 0,00");
            
            System.out.println("Erro ao carregar resumo: " + e.getMessage());
        }
    }

	private void abrirContas() {
		ContaWindow contaWindow = new ContaWindow(this, usuarioLogado);
	    contaWindow.setVisible(true);
	    this.setVisible(false);
	}

	private void abrirLancamentos() {
		LancamentoWindow lancamentoWindow = new LancamentoWindow(this, usuarioLogado);
		lancamentoWindow.setVisible(true);
		this.setVisible(false);
		System.out.println("Abrir módulo de Lançamentos");
	}

	private void abrirCategorias() {
		CategoriaWindow categoriaWindow = new CategoriaWindow(this, usuarioLogado);
		categoriaWindow.setVisible(true);
		this.setVisible(false);
		System.out.println("Abrir módulo de Categorias");
	}

	private void abrirMetas() {
		MetaWindow metaWindow = new MetaWindow(this, usuarioLogado);
		metaWindow.setVisible(true);
		this.setVisible(false);
		System.out.println("Abrir módulo de Metas");
	}

	private void abrirRelatorios() {
		RelatorioWindow relatorioWindow = new RelatorioWindow(this, usuarioLogado);
		relatorioWindow.setVisible(true);
		this.setVisible(false);
		System.out.println("Abrir módulo de Relatórios");
	}

	private void sair() throws IOException, SQLException {
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.setVisible(true);
		this.dispose();
	}
	
	private void abrirTransferencias() {
	    TransferenciaWindow transferenciaWindow = new TransferenciaWindow(this, usuarioLogado);
	    transferenciaWindow.setVisible(true);
	    this.setVisible(false);
	}

	public void inicializarComponentes() {
		setTitle("Sistema Financeiro - Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnCadastros = new JMenu("Cadastros");
		menuBar.add(mnCadastros);

		JMenuItem mntmContas = new JMenuItem("Contas Bancárias");
		mntmContas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirContas();
			}
		});
		mnCadastros.add(mntmContas);

		JMenuItem mntmCategorias = new JMenuItem("Categorias");
		mntmCategorias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCategorias();
			}
		});
		mnCadastros.add(mntmCategorias);

		JMenuItem mntmLancamentos = new JMenuItem("Lançamentos");
		mntmLancamentos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirLancamentos();
			}
		});
		mnCadastros.add(mntmLancamentos);

		JMenuItem mntmMetas = new JMenuItem("Metas Financeiras");
		mntmMetas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirMetas();
			}
		});
		mnCadastros.add(mntmMetas);
		
		JMenuItem mntmTransferencias = new JMenuItem("Transferências");
		mntmTransferencias.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        abrirTransferencias();
		    }
		});
		mnCadastros.add(mntmTransferencias);

		JMenu mnRelatorios = new JMenu("Relatórios");
		menuBar.add(mnRelatorios);

		JMenuItem mntmRelatorios = new JMenuItem("Relatórios Financeiros");
		mntmRelatorios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirRelatorios();
			}
		});
		mnRelatorios.add(mntmRelatorios);

		JMenu mnSistema = new JMenu("Sistema");
		menuBar.add(mnSistema);

		JMenuItem mntmSair = new JMenuItem("Sair");
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sair();
				} catch (IOException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnSistema.add(mntmSair);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBemVindo = new JLabel("Bem-vindo, " + usuarioLogado.getNomeCompleto());
		lblBemVindo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblBemVindo.setBounds(20, 20, 400, 30);
		contentPane.add(lblBemVindo);

		JSeparator separator = new JSeparator();
		separator.setBounds(20, 60, 740, 2);
		contentPane.add(separator);

		JLabel lblResumo = new JLabel("Resumo Financeiro");
		lblResumo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblResumo.setBounds(20, 80, 200, 25);
		contentPane.add(lblResumo);

		lblSaldoTotal = new JLabel("Saldo Total: R$ 0,00");
		lblSaldoTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSaldoTotal.setBounds(40, 120, 200, 20);
		contentPane.add(lblSaldoTotal);

		lblReceitasMes = new JLabel("Receitas do Mês: R$ 0,00");
		lblReceitasMes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblReceitasMes.setBounds(40, 150, 200, 20);
		contentPane.add(lblReceitasMes);

		lblDespesasMes = new JLabel("Despesas do Mês: R$ 0,00");
		lblDespesasMes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDespesasMes.setBounds(40, 180, 200, 20);
		contentPane.add(lblDespesasMes);

		lblSaldoMes = new JLabel("Saldo do Mês: R$ 0,00");
		lblSaldoMes.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSaldoMes.setBounds(40, 210, 200, 20);
		contentPane.add(lblSaldoMes);

		JSeparator separator2 = new JSeparator();
		separator2.setBounds(20, 250, 740, 2);
		contentPane.add(separator2);

		JLabel lblAcoesRapidas = new JLabel("Ações Rápidas");
		lblAcoesRapidas.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAcoesRapidas.setBounds(20, 270, 200, 25);
		contentPane.add(lblAcoesRapidas);

	}
}