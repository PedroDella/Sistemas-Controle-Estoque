package view;

import java.net.URL;

// IMPORT DOS COMPONENTES DO SWING
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

// IMPORT DAS CORES E FONTS
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// IMPORT DAS TELAS DE PRODUTOS
import view.ajuda.SobreView;
import view.produto.CadastroProdutoView;
import view.produto.ConsultarProdutoView;
import view.produto.ReajustarPrecoProdutoView;
import view.produto.ReajustarPrecoProdutoUnitarioView;

// IMPORT DAS TELAS DE CATEGORIAS
import view.categoria.CadastrarCategoriaView;
import view.categoria.ConsultarCategoriasView;

// IMPORT DAS TELAS DE MOVIMENTAÇÕES
import view.movimentacao.EntradaEstoqueView;
import view.movimentacao.SaidaEstoqueView;
import view.movimentacao.HistoricoEstoqueView;

// IMPORT DAS TELAS DE RELATÓRIOS
import view.relatorios.RelatorioAbaixoMinimoView;
import view.relatorios.RelatorioAcimaMaximoView;
import view.relatorios.RelatorioPorCategoriaView;
import view.relatorios.RelatorioPrecosView;
import view.relatorios.RelatorioBalancoView;

// IMPORT DAS TELAS DE AJUDA
import view.ajuda.DuvidasFrequentesView;
import view.ajuda.SuporteView;


/**
 * Classe principal da aplicação que exibe a tela inicial do sistema.
 * Contém o menu principal com acesso a todas as funcionalidades.
 * Repaginada com cabeçalho, cores de fundo e fontes ajustadas.
 */

public class TelaPrincipal extends JFrame {

    // Componentes da interface
    private JMenuBar menuBar;
    private JMenu menuProdutos, menuCategorias, menuMovimentacoes, menuRelatorios, menuAjuda;
    private JMenuItem miCadastrarProduto, miConsultarProdutos, miReajustarPrecos, miReajustarPrecosUnitario;
    private JMenuItem miCadastrarCategoria, miConsultarCategorias;
    private JMenuItem miEntradaEstoque, miSaidaEstoque, miHistoricoEstoque;
    private JMenuItem miRelatorioPrecos, miRelatorioBalanco, miRelatorioAbaixoMinimo;
    private JMenuItem miRelatorioAcimaMaximo, miRelatorioPorCategoria;
    private JMenuItem miSobre, miDuvidasFrequentes, miSuporte;

    private JPanel painelPrincipal;
    private JLabel lblCabecalho;
    private JLabel lblStatus;

    /**
     * Construtor da classe TelaPrincipal.
     * Inicializa a interface gráfica com estilo aprimorado.
     */

    public TelaPrincipal() {
        // Configurações básicas da janela
        setTitle("Sistema de Controle de Estoque");
        // Configurar tela cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Inicializa componentes
        inicializarComponentes();

        // Configura layout e estilo
        configurarLayout();

        // Configura eventos
        configurarEventos();

        // Exibe a janela
        setVisible(true);
    }

    /**
     * Inicializa os componentes da interface e define tooltips e cores.
     */

    private void inicializarComponentes() {
        // ============================
        // 1) Barra de menu personalizada
        // ============================
        menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(45, 62, 80)); // tom escuro
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        // ============================
        // 2) Menu "Produtos" e itens
        // ============================
        menuProdutos = new JMenu("Produtos");
        menuProdutos.setOpaque(true);
        menuProdutos.setBackground(new Color(45, 62, 80));
        menuProdutos.setForeground(Color.WHITE);
        // Certifica-se de que o popup (subitens) ficará escuro:
        menuProdutos.getPopupMenu().setOpaque(true);
        menuProdutos.getPopupMenu().setBackground(new Color(45, 62, 80));

        miCadastrarProduto = new JMenuItem("Cadastrar");
        miCadastrarProduto.setOpaque(true);
        miCadastrarProduto.setBackground(new Color(69, 94, 116));
        miCadastrarProduto.setForeground(Color.WHITE);
        miCadastrarProduto.setToolTipText("Cadastrar um novo produto no sistema");

        miConsultarProdutos = new JMenuItem("Consultar");
        miConsultarProdutos.setOpaque(true);
        miConsultarProdutos.setBackground(new Color(69, 94, 116));
        miConsultarProdutos.setForeground(Color.WHITE);
        miConsultarProdutos.setToolTipText("Listar e buscar produtos já cadastrados");

        miReajustarPrecos = new JMenuItem("Reajustar Preços em Massa");
        miReajustarPrecos.setOpaque(true);
        miReajustarPrecos.setBackground(new Color(69, 94, 116));
        miReajustarPrecos.setForeground(Color.WHITE);
        miReajustarPrecos.setToolTipText("Aplicar reajuste de preços a todos os produtos");

        miReajustarPrecosUnitario = new JMenuItem("Reajustar Preço Unitário");
        miReajustarPrecosUnitario.setOpaque(true);
        miReajustarPrecosUnitario.setBackground(new Color(69, 94, 116));
        miReajustarPrecosUnitario.setForeground(Color.WHITE);
        miReajustarPrecosUnitario.setToolTipText("Aplicar reajuste de preços a um produto especifico");

        menuProdutos.add(miCadastrarProduto);
        menuProdutos.add(miConsultarProdutos);
        menuProdutos.addSeparator();
        menuProdutos.add(miReajustarPrecos);
        menuProdutos.add(miReajustarPrecosUnitario);

        // ============================
        // 3) Menu "Categorias" e itens
        // ============================
        menuCategorias = new JMenu("Categorias");
        menuCategorias.setOpaque(true);
        menuCategorias.setBackground(new Color(45, 62, 80));
        menuCategorias.setForeground(Color.WHITE);
        menuCategorias.getPopupMenu().setOpaque(true);
        menuCategorias.getPopupMenu().setBackground(new Color(45, 62, 80));

        miCadastrarCategoria = new JMenuItem("Cadastrar");
        miCadastrarCategoria.setOpaque(true);
        miCadastrarCategoria.setBackground(new Color(69, 94, 116));
        miCadastrarCategoria.setForeground(Color.WHITE);
        miCadastrarCategoria.setToolTipText("Cadastrar uma nova categoria");

        miConsultarCategorias = new JMenuItem("Consultar");
        miConsultarCategorias.setOpaque(true);
        miConsultarCategorias.setBackground(new Color(69, 94, 116));
        miConsultarCategorias.setForeground(Color.WHITE);
        miConsultarCategorias.setToolTipText("Listar e buscar categorias cadastradas");

        menuCategorias.add(miCadastrarCategoria);
        menuCategorias.add(miConsultarCategorias);

        // ============================
        // 4) Menu "Movimentações" e itens
        // ============================
        menuMovimentacoes = new JMenu("Movimentações");
        menuMovimentacoes.setOpaque(true);
        menuMovimentacoes.setBackground(new Color(45, 62, 80));
        menuMovimentacoes.setForeground(Color.WHITE);
        menuMovimentacoes.getPopupMenu().setOpaque(true);
        menuMovimentacoes.getPopupMenu().setBackground(new Color(45, 62, 80));

        miEntradaEstoque = new JMenuItem("Entrada");
        miEntradaEstoque.setOpaque(true);
        miEntradaEstoque.setBackground(new Color(69, 94, 116));
        miEntradaEstoque.setForeground(Color.WHITE);
        miEntradaEstoque.setToolTipText("Registrar entrada de estoque");

        miSaidaEstoque = new JMenuItem("Saída");
        miSaidaEstoque.setOpaque(true);
        miSaidaEstoque.setBackground(new Color(69, 94, 116));
        miSaidaEstoque.setForeground(Color.WHITE);
        miSaidaEstoque.setToolTipText("Registrar saída de estoque");

        miHistoricoEstoque = new JMenuItem("Histórico de Movimentações");
        miHistoricoEstoque.setOpaque(true);
        miHistoricoEstoque.setBackground(new Color(69, 94, 116));
        miHistoricoEstoque.setForeground(Color.WHITE);
        miHistoricoEstoque.setToolTipText("Mostra o histórico completo do estoque");

        menuMovimentacoes.add(miEntradaEstoque);
        menuMovimentacoes.add(miSaidaEstoque);
        menuMovimentacoes.addSeparator();
        menuMovimentacoes.add(miHistoricoEstoque);

        // ============================
        // 5) Menu "Relatórios" e itens
        // ============================
        menuRelatorios = new JMenu("Relatórios");
        menuRelatorios.setOpaque(true);
        menuRelatorios.setBackground(new Color(45, 62, 80));
        menuRelatorios.setForeground(Color.WHITE);
        menuRelatorios.getPopupMenu().setOpaque(true);
        menuRelatorios.getPopupMenu().setBackground(new Color(45, 62, 80));

        miRelatorioPrecos = new JMenuItem("Lista de Preços");
        miRelatorioPrecos.setOpaque(true);
        miRelatorioPrecos.setBackground(new Color(69, 94, 116));
        miRelatorioPrecos.setForeground(Color.WHITE);
        miRelatorioPrecos.setToolTipText("Gerar relatório de lista de preços");

        miRelatorioBalanco = new JMenuItem("Balanço Físico/Financeiro");
        miRelatorioBalanco.setOpaque(true);
        miRelatorioBalanco.setBackground(new Color(69, 94, 116));
        miRelatorioBalanco.setForeground(Color.WHITE);
        miRelatorioBalanco.setToolTipText("Gerar relatório de balanço do estoque");

        miRelatorioAbaixoMinimo = new JMenuItem("Produtos Abaixo do Mínimo");
        miRelatorioAbaixoMinimo.setOpaque(true);
        miRelatorioAbaixoMinimo.setBackground(new Color(69, 94, 116));
        miRelatorioAbaixoMinimo.setForeground(Color.WHITE);
        miRelatorioAbaixoMinimo.setToolTipText("Gerar relatório de produtos abaixo da quantidade mínima");

        miRelatorioAcimaMaximo = new JMenuItem("Produtos Acima do Máximo");
        miRelatorioAcimaMaximo.setOpaque(true);
        miRelatorioAcimaMaximo.setBackground(new Color(69, 94, 116));
        miRelatorioAcimaMaximo.setForeground(Color.WHITE);
        miRelatorioAcimaMaximo.setToolTipText("Gerar relatório de produtos acima da quantidade máxima");

        miRelatorioPorCategoria = new JMenuItem("Produtos por Categoria");
        miRelatorioPorCategoria.setOpaque(true);
        miRelatorioPorCategoria.setBackground(new Color(69, 94, 116));
        miRelatorioPorCategoria.setForeground(Color.WHITE);
        miRelatorioPorCategoria.setToolTipText("Gerar relatório de produtos agrupados por categoria");

        menuRelatorios.add(miRelatorioPrecos);
        menuRelatorios.add(miRelatorioBalanco);
        menuRelatorios.addSeparator();
        menuRelatorios.add(miRelatorioAbaixoMinimo);
        menuRelatorios.add(miRelatorioAcimaMaximo);
        menuRelatorios.add(miRelatorioPorCategoria);

        // ============================
        // 6) Menu "Ajuda" (conteúdo central)
        // ============================
        menuAjuda = new JMenu("Ajuda");
        menuAjuda.setOpaque(true);
        menuAjuda.setBackground(new Color(45, 62, 80));
        menuAjuda.setForeground(Color.WHITE);
        menuAjuda.getPopupMenu().setOpaque(true);
        menuAjuda.getPopupMenu().setBackground(new Color(45, 62, 80));

        miSobre = new JMenuItem("Sobre");
        miSobre.setOpaque(true);
        miSobre.setBackground(new Color(69, 94, 116));
        miSobre.setForeground(Color.WHITE);
        miSobre.setToolTipText("Informações sobre o projeto e desenvolvedores");

        miDuvidasFrequentes = new JMenuItem("Dúvidas Frequentes");
        miDuvidasFrequentes.setOpaque(true);
        miDuvidasFrequentes.setBackground(new Color(69, 94, 116));
        miDuvidasFrequentes.setForeground(Color.WHITE);
        miDuvidasFrequentes.setToolTipText("Perguntas e respostas sobre como usar o sistema");

        miSuporte = new JMenuItem("Suporte");
        miSuporte.setOpaque(true);
        miSuporte.setBackground(new Color(69, 94, 116));
        miSuporte.setForeground(Color.WHITE);
        miSuporte.setToolTipText("Contato e informações para suporte técnico");

        menuAjuda.add(miSobre);
        menuAjuda.add(miDuvidasFrequentes);
        menuAjuda.add(miSuporte);

        // ============================
        // 7) Painel principal (conteúdo central)
        // ============================
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(236, 240, 241)); // cinza-claro

        // Cabeçalho grande
        lblCabecalho = new JLabel("Sistema de Controle de Estoque", JLabel.CENTER);
        lblCabecalho.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblCabecalho.setForeground(new Color(44, 62, 80)); // tom escuro
        lblCabecalho.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Barra de status na parte inferior
        lblStatus = new JLabel("Pronto", JLabel.LEFT);
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(52, 73, 94));
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    /**
     * Configura o layout da interface, adicionando cabeçalho, conteúdo e barra de status.
     */

    private void configurarLayout() {
        // Define a barra de menu personalizada
        setJMenuBar(menuBar);

        // Adiciona menus na JMenuBar
        menuBar.add(menuProdutos);
        menuBar.add(menuCategorias);
        menuBar.add(menuMovimentacoes);
        menuBar.add(menuRelatorios);
        menuBar.add(menuAjuda);

        // Painel de conteúdo (apenas um espaço vazio ampliável)
        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(new Color(236, 240, 241));

        //  Adiciona a Logo
        URL imagemUrl = getClass().getClassLoader().getResource("img/Logo_A3.png");
        if (imagemUrl != null) {
            JLabel lblLogo = new JLabel(new ImageIcon(imagemUrl));
            lblLogo.setHorizontalAlignment(JLabel.CENTER);
            painelCentro.add(lblLogo, BorderLayout.CENTER);
        } else {
            System.err.println("Imagem não encontrada!");
        }

        // Adiciona cabeçalho e conteúdo ao painel principal
        painelPrincipal.add(lblCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(lblStatus, BorderLayout.SOUTH);

        // Adiciona painel principal à janela
        add(painelPrincipal);
    }

    /**
     * Configura os eventos dos componentes de menu.
     */

    private void configurarEventos() {
        // ============================
        // Produtos
        // ============================
        miCadastrarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de cadastro de produto...");
                new CadastroProdutoView().setVisible(true);
            }
        });

        miConsultarProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de consulta de produto...");
                new ConsultarProdutoView().setVisible(true);
            }
        });

        miReajustarPrecos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de reajuste de preços...");
                new ReajustarPrecoProdutoView().setVisible(true);
            }
        });

        miReajustarPrecosUnitario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de reajuste unitário de preço...");
                new ReajustarPrecoProdutoUnitarioView().setVisible(true);
            }
        });

        // ============================
        // Categorias
        // ============================
        miCadastrarCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de cadastro de categoria...");
                new CadastrarCategoriaView().setVisible(true);
            }
        });

        miConsultarCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de consulta de categoria...");
                new ConsultarCategoriasView().setVisible(true);
            }
        });

        // ============================
        // Movimentações
        // ============================
        miEntradaEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de entrada de estoque...");
                new EntradaEstoqueView().setVisible(true);
            }
        });

        miSaidaEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de saída de estoque...");
                new SaidaEstoqueView().setVisible(true);
            }
        });

        miHistoricoEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HistoricoEstoqueView().setVisible(true);
            }
        });

        // ============================
        // Relatórios
        // ============================
        miRelatorioPrecos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Gerando relatório de preços...");
                new RelatorioPrecosView().setVisible(true);
            }
        });

        miRelatorioBalanco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Gerando relatório de balanço...");
                new RelatorioBalancoView().setVisible(true);
            }
        });

        miRelatorioAbaixoMinimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Gerando relatório de produtos abaixo do mínimo...");
                new RelatorioAbaixoMinimoView().setVisible(true);
            }
        });

        miRelatorioAcimaMaximo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Gerando relatório de produtos acima do máximo...");
                new RelatorioAcimaMaximoView().setVisible(true);
            }
        });

        miRelatorioPorCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Gerando relatório de produtos por categoria...");
                new RelatorioPorCategoriaView().setVisible(true);
            }
        });

        // ============================
        // Ajuda
        // ============================
        miSobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de sobre...");
                SobreView telaSobre = new SobreView(TelaPrincipal.this);
                telaSobre.setVisible(true);
            }
        });

        miDuvidasFrequentes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de dúvidas frequentes...");
                new DuvidasFrequentesView().setVisible(true);
            }
        });

        miSuporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblStatus.setText("Abrindo tela de suporte...");
                new SuporteView().setVisible(true);
            }
        });
    }
}