package model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma movimentação de estoque no sistema.
 * Registra entradas e saídas de produtos no estoque.
 */

public class Movimentacao {

    // Atributos da classe
    private int id;
    private Produto produto;
    private String tipo; // "Entrada" ou "Saída"
    private int quantidade;
    private LocalDateTime dataHora;
    private String observacao;

    /**
     * Construtor padrão da classe Movimentacao.
     *  - id padrão 0
     *  - produto null
     *  - tipo vazio
     *  - quantidade 0
     *  - dataHora como momento atual
     *  - observacao vazia
     */

    public Movimentacao() {
        this(0, null, "", 0, LocalDateTime.now(), "");
    }

    /**
     * Construtor da classe Movimentacao com todos os atributos.
     *
     * @param id Identificador único da movimentação
     * @param produto Produto relacionado à movimentação
     * @param tipo Tipo da movimentação ("Entrada" ou "Saída")
     * @param quantidade Quantidade movimentada
     * @param dataHora Data e hora da movimentação
     * @param observacao Observação sobre a movimentação
     */

    public Movimentacao(int id, Produto produto, String tipo, int quantidade,
                        LocalDateTime dataHora, String observacao) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
        this.observacao = observacao;
    }

    /**
     * Construtor da classe Movimentacao sem o id, útil para inserção no banco.
     * Define a data/hora como o momento atual.
     *
     * @param produto Produto relacionado à movimentação
     * @param tipo Tipo da movimentação ("Entrada" ou "Saída")
     * @param quantidade Quantidade movimentada
     * @param observacao Observação sobre a movimentação
     */

    public Movimentacao(Produto produto, String tipo, int quantidade, String observacao) {
        this(0, produto, tipo, quantidade, LocalDateTime.now(), observacao);
    }

    // Métodos getters e setters

    /**
     * Obtém o id da movimentação.
     *
     * @return O id da movimentação
     */

    public int getId() {
        return id;
    }

    /**
     * Define o id da movimentação.
     *
     * @param id O id a ser definido
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o produto relacionado à movimentação.
     *
     * @return O produto da movimentação
     */

    public Produto getProduto() {
        return produto;
    }

    /**
     * Define o produto relacionado à movimentação.
     *
     * @param produto O produto a ser definido
     */

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * Obtém o tipo da movimentação.
     *
     * @return O tipo da movimentação ("Entrada" ou "Saída")
     */

    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo da movimentação.
     *
     * @param tipo O tipo a ser definido ("Entrada" ou "Saída")
     */

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtém a quantidade movimentada.
     *
     * @return A quantidade movimentada
     */

    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade movimentada.
     *
     * @param quantidade A quantidade a ser definida
     */

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Obtém a data e hora da movimentação.
     *
     * @return A data e hora da movimentação
     */

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * Define a data e hora da movimentação.
     *
     * @param dataHora A data e hora a ser definida
     */

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * Obtém a observação sobre a movimentação.
     *
     * @return A observação da movimentação
     */

    public String getObservacao() {
        return observacao;
    }

    /**
     * Define a observação sobre a movimentação.
     *
     * @param observacao A observação a ser definida
     */

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * Verifica se a movimentação é do tipo entrada.
     *
     * @return true se for entrada, false caso contrário
     */

    public boolean isEntrada() {
        return "Entrada".equalsIgnoreCase(tipo);
    }

    /**
     * Verifica se a movimentação é do tipo saída.
     *
     * @return true se for saída, false caso contrário
     */

    public boolean isSaida() {
        return "Saída".equalsIgnoreCase(tipo);
    }

    /**
     * Atualiza o estoque do produto com base no tipo de movimentação.
     * Aumenta o estoque se for entrada, diminui se for saída.
     *
     * @return true se a operação foi bem-sucedida, false caso contrário
     */

    public boolean atualizarEstoque() {
        if (produto == null) {
            return false;
        }

        int estoqueAtual = produto.getQuantidadeEstoque();

        if (isEntrada()) {
            produto.setQuantidadeEstoque(estoqueAtual + quantidade);
            return true;
        } else if (isSaida()) {
            if (estoqueAtual >= quantidade) {
                produto.setQuantidadeEstoque(estoqueAtual - quantidade);
                return true;
            } else {
                return false; // Estoque insuficiente
            }
        }

        return false; // Tipo de movimentação inválido
    }

    /**
     * Verifica se a movimentação resultará em estoque abaixo do mínimo.
     *
     * @return true se ficar abaixo do mínimo, false caso contrário
     */

    public boolean resultaraEmEstoqueAbaixoDoMinimo() {
        if (produto == null || !isSaida()) {
            return false;
        }

        int estoqueResultante = produto.getQuantidadeEstoque() - quantidade;
        return estoqueResultante < produto.getQuantidadeMinima();
    }

    /**
     * Verifica se a movimentação resultará em estoque acima do máximo.
     *
     * @return true se ficar acima do máximo, false caso contrário
     */

    public boolean resultaraEmEstoqueAcimaDoMaximo() {
        if (produto == null || !isEntrada()) {
            return false;
        }

        int estoqueResultante = produto.getQuantidadeEstoque() + quantidade;
        return estoqueResultante > produto.getQuantidadeMaxima();
    }

    /**
     * Retorna uma representação em String da movimentação.
     *
     * @return Uma String representando a movimentação
     */

    @Override
    public String toString() {
        return tipo + " de " + quantidade + " " + (produto != null ? produto.getUnidade() : "unidades") +
                " de " + (produto != null ? produto.getNome() : "produto") +
                " em " + dataHora;
    }
}