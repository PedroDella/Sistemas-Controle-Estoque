package view.produto;

import dao.CategoriaDAO;
import dao.ProdutoDAO;
import model.Produto;
import model.Categoria;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Tela para cadastro de um novo produto.
 * Remove o campo de ID (auto-increment do banco) e usa JComboBox para escolher categoria.
 */
public class CadastroProdutoView extends JFrame {

    private JTextField tfNome, tfPreco, tfEstoque, tfMinimo, tfMaximo;
    private JComboBox<String> cbUnidade;
    private JComboBox<Categoria> cbCategoria;
    private JButton btnSalvar, btnCancelar;

    public CadastroProdutoView() {
        setTitle("Cadastrar Produto");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Painel principal com borda
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Layout de formulário (7 linhas, 2 colunas)
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        // Campos de texto
        tfNome    = new JTextField();
        tfPreco   = new JTextField();
        cbUnidade = new JComboBox<>(new String[]{
                "Lata", "Pacote", "Garrafa", "Caixa", "Saco", "Envelope", "Pote"
        });
        tfEstoque = new JTextField();
        tfMinimo  = new JTextField();
        tfMaximo  = new JTextField();
        cbCategoria = new JComboBox<>();

        // Adiciona rótulos + campos
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(tfNome);

        formPanel.add(new JLabel("Preço Unitário:"));
        formPanel.add(tfPreco);

        formPanel.add(new JLabel("Unidade:"));
        formPanel.add(cbUnidade);

        formPanel.add(new JLabel("Qtd Estoque:"));
        formPanel.add(tfEstoque);

        formPanel.add(new JLabel("Qtd Mínima:"));
        formPanel.add(tfMinimo);

        formPanel.add(new JLabel("Qtd Máxima:"));
        formPanel.add(tfMaximo);

        // Categoria via JComboBox
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(cbCategoria);

        // Carrega categorias do banco
        carregarCategorias();

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar   = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        // Junta os painéis
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Ação do botão Salvar
        btnSalvar.addActionListener((ActionEvent e) -> {
            try {
                Produto p = new Produto();
                p.setNome(tfNome.getText());
                p.setPrecoUnitario(Double.parseDouble(tfPreco.getText()));
                p.setUnidade((String) cbUnidade.getSelectedItem());
                p.setQuantidadeEstoque(Integer.parseInt(tfEstoque.getText()));
                p.setQuantidadeMinima(Integer.parseInt(tfMinimo.getText()));
                p.setQuantidadeMaxima(Integer.parseInt(tfMaximo.getText()));

                Categoria catSelecionada = (Categoria) cbCategoria.getSelectedItem();
                if (catSelecionada == null) {
                    JOptionPane.showMessageDialog(this,
                            "Selecione uma categoria válida.",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                p.setCategoria(catSelecionada);

                int gerado = new ProdutoDAO().inserir(p);
                if (gerado > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Produto salvo com sucesso! ID gerado: " + gerado,
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Não foi possível salvar o produto.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException exNum) {
                JOptionPane.showMessageDialog(this,
                        "Informe valores numéricos válidos nos campos:\n"
                                + "Preço, Estoque, Mínimo e Máximo.",
                        "Erro de Formato",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar produto: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão Cancelar
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    /**
     * Carrega todas as categorias existentes no banco e preenche o JComboBox.
     */

    private void carregarCategorias() {
        try {
            List<Categoria> lista = new CategoriaDAO().listarTodos();
            for (Categoria c : lista) {
                cbCategoria.addItem(c);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar categorias: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}