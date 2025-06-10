package view.categoria;

import dao.CategoriaDAO;
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
import java.awt.event.ActionListener;

public class CadastrarCategoriaView extends JFrame {

    private JTextField txtNome;
    private JComboBox<String> cbTamanho, cbEmbalagem;
    private JButton btnSalvar, btnCancelar;

    public CadastrarCategoriaView() {
        setTitle("Cadastrar Categoria");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelCampos.add(new JLabel("Nome da Categoria:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Tamanho:"));
        cbTamanho = new JComboBox<>(new String[]{"Pequeno", "Médio", "Grande"});
        painelCampos.add(cbTamanho);

        painelCampos.add(new JLabel("Embalagem:"));
        cbEmbalagem = new JComboBox<>(new String[]{"Lata", "Vidro", "Plástico"});
        painelCampos.add(cbEmbalagem);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText().trim();
                String tamanho = (String) cbTamanho.getSelectedItem();
                String embalagem = (String) cbEmbalagem.getSelectedItem();

                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "O nome da categoria não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Categoria categoria = new Categoria(nome, tamanho, embalagem);
                CategoriaDAO dao = new CategoriaDAO();
                int id = dao.inserir(categoria);

                if (id > 0) {
                    JOptionPane.showMessageDialog(null, "Categoria '" + nome + "' cadastrada com sucesso! ID: " + id);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}