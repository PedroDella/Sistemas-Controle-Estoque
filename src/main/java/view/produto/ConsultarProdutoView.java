package view.produto;

import dao.ProdutoDAO;
import model.Produto;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import java.util.List;

public class ConsultarProdutoView extends JFrame {

    private JTextArea textArea;

    public ConsultarProdutoView(){
        setTitle("Consultar Produtos");
        setSize(500, 400);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        try {
            List<Produto> produtos = new ProdutoDAO().listarTodos();
            for (Produto p : produtos) {
                textArea.append("ID: " + p.getId() + " - " + p.getNome() + " - R$ " + p.getPrecoUnitario() + "\n");
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(ConsultarProdutoView.this, "Erro ao consultar produtos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }
}