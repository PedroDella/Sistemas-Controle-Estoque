package view.produto;

import dao.ProdutoDAO;
import model.Produto;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

public class ReajustarPrecoProdutoUnitarioView extends JFrame {
    private JComboBox<Produto> cbProduto;
    private JTextField tfValor;
    private JButton btnAjustar, btnCancelar;
    private JRadioButton rbPorcentagem, rbValorDireto;

    public ReajustarPrecoProdutoUnitarioView() {
        setTitle("Reajustar Preço de Produto");
        setSize(450, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // painel principal com margem
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // painel de inputs
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        // ComboBox para selecionar Produto
        inputPanel.add(new JLabel("Produto:"));
        cbProduto = new JComboBox<>();
        carregarProdutos();
        inputPanel.add(cbProduto);

        // Tipo de reajuste: % ou Valor fixo
        rbPorcentagem = new JRadioButton("Reajustar por %");
        rbValorDireto = new JRadioButton("Definir valor fixo");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbPorcentagem);
        bg.add(rbValorDireto);
        rbValorDireto.setSelected(true);

        inputPanel.add(rbPorcentagem);
        inputPanel.add(rbValorDireto);

        // Campo de valor ou porcentagem
        inputPanel.add(new JLabel("Valor/Porcentagem:"));
        tfValor = new JTextField();
        inputPanel.add(tfValor);

        // painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAjustar = new JButton("Reajustar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnAjustar);
        buttonPanel.add(btnCancelar);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);

        // ação ao clicar em "Reajustar"
        btnAjustar.addActionListener((ActionEvent e) -> {
            try {
                Produto selecionado = (Produto) cbProduto.getSelectedItem();
                if (selecionado == null) {
                    JOptionPane.showMessageDialog(this,
                            "Selecione um produto válido.",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = selecionado.getId();
                double valor = Double.parseDouble(tfValor.getText().trim());

                ProdutoDAO dao = new ProdutoDAO();
                int linhasAfetadas;

                if (rbPorcentagem.isSelected()) {
                    linhasAfetadas = dao.reajustarPrecoPorcentagem(id, valor);
                } else {
                    linhasAfetadas = dao.reajustarPrecoDireto(id, valor);
                }

                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Preço atualizado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Produto não encontrado ou não atualizado.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Digite um valor numérico válido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao reajustar preço: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
        setVisible(true);
    }

    /**
     * Carrega todos os produtos existentes no banco e preenche o JComboBox.
     */

    private void carregarProdutos() {
        try {
            List<Produto> lista = new ProdutoDAO().listarTodos();
            for (Produto p : lista) {
                cbProduto.addItem(p);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}