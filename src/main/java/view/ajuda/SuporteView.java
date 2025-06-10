package view.ajuda;

import javax.swing.*;
import java.awt.*;

public class SuporteView extends JFrame {

    public SuporteView() {
        setTitle("Suporte");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Contato de Suporte");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel telefone = new JLabel("Telefone: (48) 99999-9999");
        telefone.setFont(new Font("Arial", Font.PLAIN, 16));
        telefone.setHorizontalAlignment(SwingConstants.CENTER);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(telefone, BorderLayout.CENTER);

        add(painel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SuporteView().setVisible(true));
    }
}