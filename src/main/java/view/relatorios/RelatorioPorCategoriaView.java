package view.relatorios;

import dao.ProdutoDAO;
import model.Categoria;
import model.Produto;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.JScrollPane;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.BorderLayout;

import java.util.List;
import java.util.Map;

public class RelatorioPorCategoriaView extends JFrame {

    private JTree treeProdutos;

    public RelatorioPorCategoriaView() {
        setTitle("Relatório - Produtos por Categoria");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        carregarDados();
    }

    private void inicializarComponentes() {
        treeProdutos = new JTree();
        JScrollPane scroll = new JScrollPane(treeProdutos);

        add(scroll, BorderLayout.CENTER);
    }

    private void carregarDados() {
        ProdutoDAO dao = new ProdutoDAO();
        Map<Categoria, List<Produto>> mapa = dao.listarProdutosPorCategoria();

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Categorias");

        for (Categoria categoria : mapa.keySet()) {
            DefaultMutableTreeNode noCategoria = new DefaultMutableTreeNode(categoria.getNome());

            List<Produto> produtos = mapa.get(categoria);
            for (Produto p : produtos) {
                String produtoInfo = String.format("%s - R$ %.2f - Estoque: %d %s",
                        p.getNome(),
                        p.getPrecoUnitario(),
                        p.getQuantidadeEstoque(),
                        p.getUnidade());
                noCategoria.add(new DefaultMutableTreeNode(produtoInfo));
            }

            raiz.add(noCategoria);
        }

        DefaultTreeModel modelo = new DefaultTreeModel(raiz);
        treeProdutos.setModel(modelo);
        treeProdutos.expandRow(0);
    }
}