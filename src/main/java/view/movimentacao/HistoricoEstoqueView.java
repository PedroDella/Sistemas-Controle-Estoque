package view.movimentacao;

import dao.MovimentacaoDAO;
import model.Movimentacao;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoricoEstoqueView extends JFrame {

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnFechar;

    public HistoricoEstoqueView() {
        setTitle("Histórico de Movimentações");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] colunas = {
                "ID", "Produto", "Tipo", "Quantidade", "Data/Hora", "Observação"
        };

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // todas as células não editáveis
            }
        };

        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnFechar);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        carregarDados();

        setVisible(true);
    }

    private void carregarDados() {
        MovimentacaoDAO dao = new MovimentacaoDAO();
        List<Movimentacao> lista = dao.listarTodos();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Movimentacao m : lista) {
            Object[] row = {
                    m.getId(),
                    m.getProduto().getNome(),
                    m.getTipo(),
                    m.getQuantidade(),
                    m.getDataHora().format(formatter),
                    m.getObservacao()
            };
            tableModel.addRow(row);
        }
    }
}