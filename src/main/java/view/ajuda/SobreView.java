package view.ajuda;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.Frame;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class SobreView extends JDialog {
    private JTextArea textArea;
    private JButton btnFechar;

    public SobreView(Frame parent) {
        super(parent, "Sobre o Sistema", true);
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        pack();
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setText("Sistema de Controle de Estoque\n\n" +
                "Este sistema foi desenvolvido para a disciplina de Programação de Soluções Computacionais\n" +
                "da Universidade do Sul de Santa Catarina - UNISUL.\n\n" +
                "Integrantes do Grupo:\n" +
                "- Arthur Zamprogna Ventura\n" +
                "- Gabriel Luipo\n" +
                "- Nícolas Gaia Negrão\n" +
                "- Pedro Henrique Francio Della Giustina\n" +
                "- Thiago da Silveira Gentil\n\n" +
                "Funcionalidades Principais:\n" +
                "- Cadastro e gerenciamento de produtos\n" +
                "- Controle de categorias\n" +
                "- Movimentações de estoque\n" +
                "- Geração de relatórios\n\n" +
                "Versão: 1.0\n" +
                "© 2025 - MIT LICENSE");

        btnFechar = new JButton("Fechar");
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));

        // Configurar margens
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adicionar texto em um JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel para o botão
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnFechar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void configurarEventos() {
        btnFechar.addActionListener(e -> dispose());
    }
}