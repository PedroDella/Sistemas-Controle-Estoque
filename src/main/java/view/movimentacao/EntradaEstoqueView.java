package view.movimentacao;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import model.Movimentacao;
import model.Produto;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tela para registrar uma entrada de estoque de um produto existente.
 * Agora usa JComboBox para selecionar o produto em vez de digitar o ID.
 * Valida formatos de data e hora, pré-preenche a data atual e exibe
 * mensagens de erro claras para cada campo.
 */
public class EntradaEstoqueView extends JFrame {
    private JComboBox<Produto> comboProdutos;
    private JTextField campoQuantidade;
    private JTextField campoData;
    private JTextField campoHora;
    private JTextArea campoObservacao;
    private JButton botaoSalvar, botaoCancelar;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public EntradaEstoqueView() {
        setTitle("Registrar Entrada de Estoque");
        setSize(450, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de campos com espaçamento
        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // ComboBox de produtos (exibe toString de Produto)
        painelCampos.add(new JLabel("Produto:"));
        comboProdutos = new JComboBox<>();
        comboProdutos.setToolTipText("Selecione o produto para registrar a entrada");
        carregarProdutosNoCombo();
        painelCampos.add(comboProdutos);

        // Campo “Quantidade”
        painelCampos.add(new JLabel("Quantidade (unidades):"));
        campoQuantidade = new JTextField();
        campoQuantidade.setToolTipText("Informe a quantidade (número inteiro)");
        painelCampos.add(campoQuantidade);

        // Campo “Data” (pré-preenchido com data atual)
        painelCampos.add(new JLabel("Data (YYYY-MM-DD):"));
        campoData = new JTextField(LocalDate.now().format(FORMATO_DATA));
        campoData.setToolTipText("Formato: 2023-07-21 (se vazio, assume data atual)");
        painelCampos.add(campoData);

        // Campo “Hora” (pode ficar vazio para 00:00)
        painelCampos.add(new JLabel("Hora (HH:mm):"));
        campoHora = new JTextField(LocalTime.now().format(FORMATO_HORA));
        campoHora.setToolTipText("Formato: 14:30 (se vazio, assume 00:00)");
        painelCampos.add(campoHora);

        // Campo “Observação”
        painelCampos.add(new JLabel("Observação (opcional):"));
        campoObservacao = new JTextArea(3, 20);
        campoObservacao.setLineWrap(true);
        campoObservacao.setWrapStyleWord(true);
        campoObservacao.setToolTipText("Observações adicionais sobre esta entrada");
        JScrollPane scrollObs = new JScrollPane(campoObservacao);
        painelCampos.add(scrollObs);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botaoSalvar = new JButton("Salvar Entrada");
        botaoCancelar = new JButton("Cancelar");
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Listener do botão Salvar
        botaoSalvar.addActionListener((ActionEvent e) -> {
            try {
                Produto selecionado = (Produto) comboProdutos.getSelectedItem();
                if (selecionado == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Nenhum produto selecionado.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                int quantidade = parseInteiro(campoQuantidade.getText().trim(), "Quantidade");

                LocalDateTime dataHora = parseDataHora(
                        campoData.getText().trim(),
                        campoHora.getText().trim()
                );

                String observacao = campoObservacao.getText().trim();

                Movimentacao m = new Movimentacao();
                m.setProduto(selecionado);
                m.setQuantidade(quantidade);
                m.setTipo("Entrada");
                m.setDataHora(dataHora);
                m.setObservacao(observacao);

                new MovimentacaoDAO().inserir(m);

                JOptionPane.showMessageDialog(
                        this,
                        "Entrada registrada com sucesso para \"" + selecionado.getNome() + "\" em "
                                + dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } catch (NumberFormatException exNum) {
                // Mensagem específica já lançada em parseInteiro
            } catch (IllegalArgumentException exArg) {
                JOptionPane.showMessageDialog(
                        this,
                        exArg.getMessage(),
                        "Erro de Formato",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao registrar entrada: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Listener do botão Cancelar
        botaoCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    /**
     * Carrega todos os produtos existentes no banco e preenche o JComboBox<Produto>.
     */
    private void carregarProdutosNoCombo() {
        try {
            List<Produto> lista = new ProdutoDAO().listarTodos();
            for (Produto p : lista) {
                comboProdutos.addItem(p);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar produtos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Converte uma string em inteiro, lançando NumberFormatException
     * com mensagem amigável se estiver vazia ou inválida.
     */
    private int parseInteiro(String texto, String nomeCampo) {
        if (texto.isEmpty()) {
            throw new NumberFormatException(nomeCampo + " não pode ficar vazio.");
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(nomeCampo + " deve ser um número inteiro válido.");
        }
    }

    /**
     * Converte strings de data e hora em LocalDateTime.
     * Se data estiver vazia, usa a data atual. Se hora vazia, assume 00:00.
     * Lança IllegalArgumentException para formatos inválidos.
     */
    private LocalDateTime parseDataHora(String dataStr, String horaStr) {
        LocalDate data;
        if (dataStr.isEmpty()) {
            data = LocalDate.now();
        } else {
            try {
                data = LocalDate.parse(dataStr, FORMATO_DATA);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Data inválida. Use formato YYYY-MM-DD.");
            }
        }

        LocalTime hora;
        if (horaStr.isEmpty()) {
            hora = LocalTime.of(0, 0);
        } else {
            try {
                hora = LocalTime.parse(horaStr, FORMATO_HORA);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Hora inválida. Use formato HH:mm.");
            }
        }

        return LocalDateTime.of(data, hora);
    }
}
