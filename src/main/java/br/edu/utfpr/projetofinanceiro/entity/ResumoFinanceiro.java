package br.edu.utfpr.projetofinanceiro.entity;

public class ResumoFinanceiro {
    private double saldoTotal;
    private double receitasMes;
    private double despesasMes;
    private double saldoMes;
    
    // Construtor
    public ResumoFinanceiro(double saldoTotal, double receitasMes, double despesasMes, double saldoMes) {
        this.saldoTotal = saldoTotal;
        this.receitasMes = receitasMes;
        this.despesasMes = despesasMes;
        this.saldoMes = saldoMes;
    }
    
    // Getters
    public double getSaldoTotal() { return saldoTotal; }
    public double getReceitasMes() { return receitasMes; }
    public double getDespesasMes() { return despesasMes; }
    public double getSaldoMes() { return saldoMes; }
}
