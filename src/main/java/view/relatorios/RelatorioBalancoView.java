package view.relatorios;

import dao.ProdutoDAO;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;

import java.text.NumberFormat;
import java.util.Locale;


public class RelatorioBalancoView extends JFrame {

    private JLabel lblValor;

    public RelatorioBalancoView() {
        setTitle("Relatório - Balanço do Estoque");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        carregarValorTotal();
    }

    private void inicializarComponentes() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblValor = new JLabel("Calculando...", SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        painel.add(lblValor, BorderLayout.CENTER);
        painel.add(btnFechar, BorderLayout.SOUTH);

        add(painel);
    }

    private void carregarValorTotal() {
        ProdutoDAO dao = new ProdutoDAO();
        double total = dao.calcularValorTotalEstoque();

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblValor.setText("Total em estoque: " + formatoMoeda.format(total));
    }
}