package view.produto;

import dao.ProdutoDAO;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;

/**
 * Tela para reajustar em massa o preço unitário de todos os produtos
 * com base em um percentual informado.
 */

public class ReajustarPrecoProdutoView extends JFrame {

    private JTextField tfPercentual;
    private JButton btnReajustar, btnCancelar;

    public ReajustarPrecoProdutoView() {
        setTitle("Reajustar Preços em Massa");
        setSize(350, 140);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Painel principal com borda
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entrada: rótulo + campo de texto
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        inputPanel.add(new JLabel("Percentual de Reajuste (%):"));
        tfPercentual = new JTextField();
        tfPercentual.setToolTipText("Ex: 10 para aumentar 10% ou -5 para diminuir 5%");
        inputPanel.add(tfPercentual);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnReajustar = new JButton("Aplicar");
        btnCancelar   = new JButton("Cancelar");
        buttonPanel.add(btnReajustar);
        buttonPanel.add(btnCancelar);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Ação do botão “Aplicar”
        btnReajustar.addActionListener((ActionEvent e) -> {
            try {
                String texto = tfPercentual.getText().trim();
                if (texto.isEmpty()) {
                    throw new NumberFormatException("Campo vazio");
                }
                double percentual = Double.parseDouble(texto);
                int qtdAtualizados = new ProdutoDAO().reajustarPrecos(percentual);
                JOptionPane.showMessageDialog(
                        this,
                        String.format("Preços reajustados em %.2f%% para %d produtos.", percentual, qtdAtualizados),
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } catch (NumberFormatException exNum) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe um número válido para percentual (ex: 10 ou -5).",
                        "Erro de Formato",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao reajustar preços: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Ação do botão “Cancelar”
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }
}