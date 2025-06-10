package view.relatorios;

import dao.ProdutoDAO;
import model.Produto;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;

import java.util.List;

public class RelatorioAcimaMaximoView extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;

    public RelatorioAcimaMaximoView() {
        setTitle("Relatório - Produtos Acima do Máximo");
        setSize(850, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        carregarDados();
    }

    private void inicializarComponentes() {
        JPanel painel = new JPanel(new BorderLayout());

        modelo = new DefaultTableModel();
        modelo.addColumn("Nome");
        modelo.addColumn("Unidade");
        modelo.addColumn("Estoque");
        modelo.addColumn("Mínimo");
        modelo.addColumn("Máximo");
        modelo.addColumn("Categoria");

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        JPanel painelInferior = new JPanel();
        painelInferior.add(btnFechar);

        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelInferior, BorderLayout.SOUTH);

        add(painel);
    }

    private void carregarDados() {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarAcimaMaximo();

        modelo.setRowCount(0);

        for (Produto p : produtos) {
            modelo.addRow(new Object[]{
                    p.getNome(),
                    p.getUnidade(),
                    p.getQuantidadeEstoque(),
                    p.getQuantidadeMinima(),
                    p.getQuantidadeMaxima(),
                    p.getCategoria().getNome()
            });
        }
    }
}