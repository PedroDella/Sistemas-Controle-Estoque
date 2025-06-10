package model;

/**
 * Classe que representa um produto no sistema de controle de estoque.
 * Contém informações como nome, preço, quantidades e categoria.
 */

public class Produto {
    
    // Atributos da classe
    private int id;
    private String nome;
    private double precoUnitario;
    private String unidade;
    private int quantidadeEstoque;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private Categoria categoria;

    /**
     * Construtor padrão da classe Produto.
     * Inicializa todos os atributos com valores “neutros” (id=0, nome/vazios, números zero, categoria nula).
     */

    public Produto() {
        this(0, "", 0.0, "", 0, 0, 0, null);
    }
    
    /**
     * Construtor da classe Produto com todos os atributos.
     * 
     * @param id Identificador único do produto
     * @param nome Nome do produto
     * @param precoUnitario Preço unitário do produto
     * @param unidade Unidade de medida do produto
     * @param quantidadeEstoque Quantidade atual em estoque
     * @param quantidadeMinima Quantidade mínima permitida em estoque
     * @param quantidadeMaxima Quantidade máxima permitida em estoque
     * @param categoria Categoria do produto
     */

    public Produto(int id, String nome, double precoUnitario, String unidade, 
                  int quantidadeEstoque, int quantidadeMinima, int quantidadeMaxima, 
                  Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.unidade = unidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.categoria = categoria;
    }
    
    /**
     * Construtor da classe Produto sem o id, útil para inserção no banco.
     * 
     * @param nome Nome do produto
     * @param precoUnitario Preço unitário do produto
     * @param unidade Unidade de medida do produto
     * @param quantidadeEstoque Quantidade atual em estoque
     * @param quantidadeMinima Quantidade mínima permitida em estoque
     * @param quantidadeMaxima Quantidade máxima permitida em estoque
     * @param categoria Categoria do produto
     */

    public Produto(String nome, double precoUnitario, String unidade, 
                  int quantidadeEstoque, int quantidadeMinima, int quantidadeMaxima, 
                  Categoria categoria) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.unidade = unidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.categoria = categoria;
    }
    
    // Métodos getters e setters
    
    /**
     * Obtém o id do produto.
     * 
     * @return O id do produto
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o id do produto.
     * 
     * @param id O id a ser definido
     */

    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome do produto.
     * 
     * @return O nome do produto
     */

    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do produto.
     * 
     * @param nome O nome a ser definido
     */

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém o preço unitário do produto.
     * 
     * @return O preço unitário
     */

    public double getPrecoUnitario() {
        return precoUnitario;
    }
    
    /**
     * Define o preço unitário do produto.
     * 
     * @param precoUnitario O preço unitário a ser definido
     */

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    /**
     * Obtém a unidade de medida do produto.
     * 
     * @return A unidade de medida
     */

    public String getUnidade() {
        return unidade;
    }
    
    /**
     * Define a unidade de medida do produto.
     * 
     * @param unidade A unidade de medida a ser definida
     */

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    /**
     * Obtém a quantidade em estoque do produto.
     * 
     * @return A quantidade em estoque
     */

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
    
    /**
     * Define a quantidade em estoque do produto.
     * 
     * @param quantidadeEstoque A quantidade em estoque a ser definida
     */

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
    
    /**
     * Obtém a quantidade mínima permitida em estoque.
     * 
     * @return A quantidade mínima
     */

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }
    
    /**
     * Define a quantidade mínima permitida em estoque.
     * 
     * @param quantidadeMinima A quantidade mínima a ser definida
     */

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
    
    /**
     * Obtém a quantidade máxima permitida em estoque.
     * 
     * @return A quantidade máxima
     */

    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }
    
    /**
     * Define a quantidade máxima permitida em estoque.
     * 
     * @param quantidadeMaxima A quantidade máxima a ser definida
     */

    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }
    
    /**
     * Obtém a categoria do produto.
     * 
     * @return A categoria do produto
     */

    public Categoria getCategoria() {
        return categoria;
    }
    
    /**
     * Define a categoria do produto.
     * 
     * @param categoria A categoria a ser definida
     */

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    /**
     * Verifica se o produto está abaixo da quantidade mínima.
     * 
     * @return true se estiver abaixo do mínimo, false caso contrário
     */

    public boolean estaAbaixoDoMinimo() {
        return quantidadeEstoque < quantidadeMinima;
    }
    
    /**
     * Verifica se o produto está acima da quantidade máxima.
     * 
     * @return true se estiver acima do máximo, false caso contrário
     */

    public boolean estaAcimaDoMaximo() {
        return quantidadeEstoque > quantidadeMaxima;
    }
    
    /**
     * Calcula o valor total do produto em estoque.
     * 
     * @return O valor total (preço unitário * quantidade em estoque)
     */

    public double getValorTotalEmEstoque() {
        return precoUnitario * quantidadeEstoque;
    }
    
    /**
     * Aplica um reajuste percentual ao preço do produto.
     * 
     * @param percentual Percentual de reajuste (ex: 10 para 10%)
     */

    public void reajustarPreco(double percentual) {
        this.precoUnitario = this.precoUnitario * (1 + (percentual / 100));
    }
    
    /**
     * Retorna uma representação em String do produto.
     * 
     * @return Uma String representando o produto
     */

    @Override
    public String toString() {
        return nome + " - " + unidade + " - R$ " + String.format("%.2f", precoUnitario);
    }
    
    /**
     * Verifica se dois produtos são iguais com base no id.
     * 
     * @param obj O objeto a ser comparado
     * @return true se os produtos forem iguais, false caso contrário
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Produto outroProduto = (Produto) obj;
        return id == outroProduto.id;
    }
    
    /**
     * Gera um código hash para o produto com base no id.
     * 
     * @return O código hash gerado
     */
    @Override
    public int hashCode() {
        return 31 * 7 + id;
    }
}