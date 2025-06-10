package view;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia a aplicação.
 * Ponto de entrada do sistema de controle de estoque.
 */

public class App {
    
    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args Argumentos de linha de comando (não utilizados)
     */

    public static void main(String[] args) {
        // Configura o look and feel para parecer com o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Erro ao configurar look and feel: " + ex.getMessage());
        }
        
        // Inicia a aplicação na thread de eventos do Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Cria e exibe a tela principal
                new TelaPrincipal();
            }
        });
    }
}