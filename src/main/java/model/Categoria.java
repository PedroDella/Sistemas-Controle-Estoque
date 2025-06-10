package model;

/**
 * Classe que representa uma categoria de produtos no sistema de controle de estoque.
 * Contém informações como nome, tamanho e tipo de embalagem.
 */

public class Categoria {

    // Atributos da classe
    private int id;
    private String nome;
    private String tamanho; // Pequeno, Médio, Grande
    private String embalagem; // Lata, Vidro, Plástico

    /**
     * Construtor padrão da classe Categoria.
     * Inicializa os atributos com valores padrão (nome vazio, tamanho vazio, embalagem vazia).
     */

    public Categoria() {
        this("", "", "");
    }

    /**
     * Construtor da classe Categoria com todos os atributos.
     *
     * @param id Identificador único da categoria
     * @param nome Nome da categoria
     * @param tamanho Tamanho da categoria (Pequeno, Médio, Grande)
     * @param embalagem Tipo de embalagem (Lata, Vidro, Plástico)
     */

    public Categoria(int id, String nome, String tamanho, String embalagem) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
    }

    /**
     * Construtor da classe Categoria sem o id, útil para inserção no banco.
     *
     * @param nome Nome da categoria
     * @param tamanho Tamanho da categoria (Pequeno, Médio, Grande)
     * @param embalagem Tipo de embalagem (Lata, Vidro, Plástico)
     */

    public Categoria(String nome, String tamanho, String embalagem) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
    }

    // Métodos getters e setters

    /**
     * Obtém o id da categoria.
     *
     * @return O id da categoria
     */

    public int getId() {
        return id;
    }

    /**
     * Define o id da categoria.
     *
     * @param id O id a ser definido
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome da categoria.
     *
     * @return O nome da categoria
     */

    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da categoria.
     *
     * @param nome O nome a ser definido
     */

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o tamanho da categoria.
     *
     * @return O tamanho da categoria
     */

    public String getTamanho() {
        return tamanho;
    }

    /**
     * Define o tamanho da categoria.
     *
     * @param tamanho O tamanho a ser definido
     */

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * Obtém o tipo de embalagem da categoria.
     *
     * @return O tipo de embalagem
     */

    public String getEmbalagem() {
        return embalagem;
    }

    /**
     * Define o tipo de embalagem da categoria.
     *
     * @param embalagem O tipo de embalagem a ser definido
     */

    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    /**
     * Retorna uma representação em String da categoria.
     *
     * @return Uma String representando a categoria
     */

    @Override
    public String toString() {
        return nome + " (" + tamanho + ", " + embalagem + ")";
    }

    /**
     * Verifica se duas categorias são iguais com base no id.
     *
     * @param obj O objeto a ser comparado
     * @return true se as categorias forem iguais, false caso contrário
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Categoria outraCategoria = (Categoria) obj;
        return id == outraCategoria.id;
    }

    /**
     * Gera um código hash para a categoria com base no id.
     *
     * @return O código hash gerado
     */

    @Override
    public int hashCode() {
        return 31 * 7 + id;
    }
}