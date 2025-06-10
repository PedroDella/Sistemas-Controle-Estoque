package view.ajuda;

import javax.swing.*;
import java.awt.*;

public class DuvidasFrequentesView extends JFrame {

    public DuvidasFrequentesView() {
        setTitle("Dúvidas Frequentes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));

        StringBuilder texto = new StringBuilder();
        texto.append("COMO UTILIZAR O SISTEMA:\n");
        texto.append("- Navegue pelo menu para acessar as funcionalidades como cadastro de produtos, categorias e movimentações.\n\n");

        texto.append("AO CADASTRAR O PRODUTO, CASO NÃO ENCONTRE A CATEGORIA:\n");
        texto.append("- Clique em 'Cancelar'.\n");
        texto.append("- Vá até a tela de 'Cadastrar Categoria'.\n");
        texto.append("- Cadastre a nova categoria desejada.\n");
        texto.append("- Volte à tela de 'Cadastrar Produto' e refaça o cadastro.\n\n");

        texto.append("DICA:\n");
        texto.append("- Sempre cadastre as categorias antes de cadastrar produtos para evitar interrupções no processo.");

        areaTexto.setText(texto.toString());

        JScrollPane scroll = new JScrollPane(areaTexto);
        add(scroll);
    }
}