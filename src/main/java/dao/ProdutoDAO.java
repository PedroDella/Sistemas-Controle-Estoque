package dao;

import model.Produto;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de acesso a dados para a entidade Produto.
 * Implementa operações de CRUD (Create, Read, Update, Delete) e consultas específicas.
 */

public class ProdutoDAO {

    // Instância da fábrica de conexões
    private final ConnectionFactory connectionFactory;

    /**
     * Construtor padrão que inicializa a fábrica de conexões.
     */

    public ProdutoDAO() {
        this.connectionFactory = new ConnectionFactory();
    }

    /**
     * Insere um novo produto no banco de dados.
     *
     * @param produto O produto a ser inserido
     * @return O ID gerado para o produto inserido ou -1 em caso de erro
     */

    public int inserir(Produto produto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idGerado = -1;

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para inserção
            String sql = "INSERT INTO produto (nome, preco_unitario, unidade, quantidade_estoque, "
                    + "quantidade_minima, quantidade_maxima, categoria_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Prepara statement para inserção
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria().getId());

            // Executa a inserção
            int linhasAfetadas = stmt.executeUpdate();

            // Verifica se a inserção foi bem-sucedida
            if (linhasAfetadas > 0) {
                // Obtém o ID gerado
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    produto.setId(idGerado);
                }
            }

            return idGerado;
        } catch (SQLException ex) {
            System.err.println("Erro ao inserir produto: " + ex.getMessage());
            return -1;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Atualiza um produto existente no banco de dados.
     *
     * @param produto O produto a ser atualizado
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */

    public boolean atualizar(Produto produto) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para atualização
            String sql = "UPDATE produto SET nome = ?, preco_unitario = ?, unidade = ?, "
                    + "quantidade_estoque = ?, quantidade_minima = ?, quantidade_maxima = ?, "
                    + "categoria_id = ? WHERE id = ?";

            // Prepara statement para atualização
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria().getId());
            stmt.setInt(8, produto.getId());

            // Executa a atualização
            int linhasAfetadas = stmt.executeUpdate();

            // Verifica se a atualização foi bem-sucedida
            return linhasAfetadas > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar produto: " + ex.getMessage());
            return false;
        } finally {
            // Fecha recursos
            fecharRecursos(null, stmt, conn);
        }
    }

    /**
     * Exclui um produto do banco de dados.
     *
     * @param id O ID do produto a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */

    public boolean excluir(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para exclusão
            String sql = "DELETE FROM produto WHERE id = ?";

            // Prepara statement para exclusão
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            // Executa a exclusão
            int linhasAfetadas = stmt.executeUpdate();

            // Verifica se a exclusão foi bem-sucedida
            return linhasAfetadas > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao excluir produto: " + ex.getMessage());
            return false;
        } finally {
            // Fecha recursos
            fecharRecursos(null, stmt, conn);
        }
    }

    /**
     * Consulta um produto pelo ID.
     *
     * @param id O ID do produto a ser consultado
     * @return O produto encontrado ou null se não existir
     */

    public Produto consultar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto produto = null;

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para consulta
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM produto p "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "WHERE p.id = ?";

            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            // Executa a consulta
            rs = stmt.executeQuery();

            // Verifica se encontrou o produto
            if (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);
            }

            return produto;
        } catch (SQLException ex) {
            System.err.println("Erro ao consultar produto: " + ex.getMessage());
            return null;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return Uma lista com todos os produtos
     */

    public List<Produto> listarTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para listar todos os produtos
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM produto p "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "ORDER BY p.id";

            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);

            // Executa a consulta
            rs = stmt.executeQuery();

            // Percorre os resultados
            while (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                produtos.add(produto);
            }

            return produtos;
        } catch (SQLException ex) {
            System.err.println("Erro ao listar produtos: " + ex.getMessage());
            return produtos;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Reajusta os preços de todos os produtos em um determinado percentual.
     *
     * @param percentual O percentual de reajuste (ex: 10 para 10%)
     * @return O número de produtos reajustados
     */

    public int reajustarPrecos(double percentual) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para reajuste de preços
            String sql = "UPDATE produto SET preco_unitario = preco_unitario * (1 + ? / 100)";

            // Prepara statement para atualização
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, percentual);

            // Executa a atualização
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao reajustar preços: " + ex.getMessage());
            return 0;
        } finally {
            // Fecha recursos
            fecharRecursos(null, stmt, conn);
        }
    }

    /**
     * Reajusta o preço de um produto específico com base em um percentual informado.
     *
     * @param idProduto O ID do produto que terá o preço reajustado.
     * @param percentual O percentual de aumento (ex: 10 para 10%).
     * @return O número de produtos atualizados (0 se nenhum for encontrado).
     */

    public int reajustarPrecoPorcentagem(int idProduto, double percentual) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionFactory.getConnection(); // corrigido
            String sql = "UPDATE produto SET preco_unitario = preco_unitario * (1 + ? / 100) WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, percentual);
            stmt.setInt(2, idProduto);

            return stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao reajustar por percentual: " + ex.getMessage());
            return 0;
        } finally {
            fecharRecursos(null, stmt, conn);
        }
    }

    /**
     * Define um novo valor unitário para um produto específico.
     *
     * @param idProduto O ID do produto que terá o preço alterado.
     * @param novoValor O novo valor unitário a ser definido para o produto.
     * @return O número de produtos atualizados (0 se nenhum for encontrado).
     */

    public int reajustarPrecoDireto(int idProduto, double novoValor) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionFactory.getConnection(); // corrigido
            String sql = "UPDATE produto SET preco_unitario = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, novoValor);
            stmt.setInt(2, idProduto);

            return stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao reajustar por valor direto: " + ex.getMessage());
            return 0;
        } finally {
            fecharRecursos(null, stmt, conn);
        }
    }

    /**
     * Lista os produtos que estão abaixo da quantidade mínima.
     *
     * @return Uma lista com os produtos abaixo da quantidade mínima
     */

    public List<Produto> listarAbaixoMinimo() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para listar produtos abaixo do mínimo
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM produto p "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "WHERE p.quantidade_estoque < p.quantidade_minima "
                    + "ORDER BY p.nome";

            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);

            // Executa a consulta
            rs = stmt.executeQuery();

            // Percorre os resultados
            while (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                produtos.add(produto);
            }

            return produtos;
        } catch (SQLException ex) {
            System.err.println("Erro ao listar produtos abaixo do mínimo: " + ex.getMessage());
            return produtos;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Lista os produtos que estão acima da quantidade máxima.
     *
     * @return Uma lista com os produtos acima da quantidade máxima
     */

    public List<Produto> listarAcimaMaximo() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para listar produtos acima do máximo
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM produto p "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "WHERE p.quantidade_estoque > p.quantidade_maxima "
                    + "ORDER BY p.nome";

            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);

            // Executa a consulta
            rs = stmt.executeQuery();

            // Percorre os resultados
            while (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                produtos.add(produto);
            }

            return produtos;
        } catch (SQLException ex) {
            System.err.println("Erro ao listar produtos acima do máximo: " + ex.getMessage());
            return produtos;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Lista todos os produtos com os seus preços unitários.
     *
     * @return Uma lista de produtos contendo nome, preço unitário, unidade, quantidade em estoque e categoria.
     */

    public List<Produto> listarComPrecos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para listar produtos com preços
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "c.id AS categoria_id, c.nome AS categoria_nome, c.tamanho, c.embalagem "
                    + "FROM produto p "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "ORDER BY p.nome";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("tamanho"));
                categoria.setEmbalagem(rs.getString("embalagem"));

                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                // Não são necessárias quantidade mínima e máxima aqui
                produto.setQuantidadeMinima(0);
                produto.setQuantidadeMaxima(0);
                produto.setCategoria(categoria);

                produtos.add(produto);
            }

            return produtos;
        } catch (SQLException ex) {
            System.err.println("Erro ao listar produtos com preços: " + ex.getMessage());
            return produtos;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Calcula o valor total do estoque, multiplicando o preço unitário pela quantidade em estoque de cada produto.
     *
     * @return O valor monetário total de todos os produtos em estoque.
     */

    public double calcularValorTotalEstoque() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double total = 0;

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para cálculo do valor total
            String sql = "SELECT SUM(preco_unitario * quantidade_estoque) AS total FROM produto";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

            return total;
        } catch (SQLException ex) {
            System.err.println("Erro ao calcular valor total do estoque: " + ex.getMessage());
            return 0;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Agrupa os produtos cadastrados por categoria.
     *
     * @return Um mapa onde a chave é a categoria e o valor é a lista de produtos pertencentes a ela.
     */

    public Map<Categoria, List<Produto>> listarProdutosPorCategoria() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<Categoria, List<Produto>> mapa = new HashMap<>();

        try {
            // Obtém conexão com o banco de dados
            conn = connectionFactory.getConnection();

            // SQL para listar produtos agrupados por categoria
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, c.id AS categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho, c.embalagem "
                    + "FROM produto p "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "ORDER BY c.nome, p.nome";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("tamanho"));
                categoria.setEmbalagem(rs.getString("embalagem"));

                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                mapa.computeIfAbsent(categoria, k -> new ArrayList<>()).add(produto);
            }

            return mapa;
        } catch (SQLException ex) {
            System.err.println("Erro ao listar produtos por categoria: " + ex.getMessage());
            return mapa;
        } finally {
            // Fecha recursos
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Fecha recursos de ResultSet, PreparedStatement e Connection.
     *
     * @param rs   ResultSet a ser fechado (pode ser null)
     * @param stmt Statement a ser fechado (pode ser null)
     * @param conn Conexão a ser fechada (pode ser null)
     */

    private void fecharRecursos(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ResultSet ou Statement: " + e.getMessage());
        } finally {
            connectionFactory.closeConnection(conn);
        }
    }
}