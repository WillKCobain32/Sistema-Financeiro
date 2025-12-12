package br.edu.utfpr.projetofinanceiro.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.edu.utfpr.projetofinanceiro.entity.Usuario;
import br.edu.utfpr.projetofinanceiro.service.ResumoFinanceiroService;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;

public class RelatorioMensal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblMes;
	private JComboBox <String> JMes;
	private ResumoFinanceiroService resumoService;
	private Usuario usuarioLogado;
	/**
	 * @wbp.nonvisual location=332,9
	 */
	private final JTextArea Relatorio = new JTextArea();
	/**
	 * @wbp.nonvisual location=382,9
	 */
	private final JTextArea mensal = new JTextArea();
	private JTable table;
	private JLabel lblSaldo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RelatorioMensal frame = new RelatorioMensal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	
	public RelatorioMensal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 373);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 53, 760, 263);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 740, 241);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Receitas", "Ganhos", "Despesas", "Gastos"
			}
		));
		
		lblMes = new JLabel("Mês");
		lblMes.setBounds(10, 28, 46, 14);
		contentPane.add(lblMes);
		
		JMes = new JComboBox();
		JMes.setBounds(42, 24, 150, 22);
		JMes.addItem("Janeiro");
		JMes.addItem("Fevereiro");
		JMes.addItem("Março");
		JMes.addItem("Abril");
		JMes.addItem("Maio");
		JMes.addItem("Junho");
		JMes.addItem("Julho");
		JMes.addItem("Agosto");
		JMes.addItem("Setemebro");
		JMes.addItem("Outubro");
		JMes.addItem("Novembro");
		JMes.addItem("Dezembro");
		contentPane.add(JMes);
		
		JLabel lblNewLabel = new JLabel("Relatorio Mensal");
		lblNewLabel.setBounds(307, 0, 79, 14);
		contentPane.add(lblNewLabel);
		
		lblSaldo = new JLabel("Saldo do mes: R$ 0.00");
		lblSaldo.setBounds(202, 28, 118, 14);
		contentPane.add(lblSaldo);

	}
}
