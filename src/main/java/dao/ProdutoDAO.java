package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Categoria;
import model.Produto;

/**
 * Classe de acesso a dados para a entidade Produto.
 * Implementa operações de CRUD (Create, Read, Update, Delete) e consultas específicas.
 */
public class ProdutoDAO {
    
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para inserção
            String sql = "INSERT INTO produto (nome, preco_unitario, unidade, quantidade_estoque, " +
                         "quantidade_minima, quantidade_maxima, categoria_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para atualização
            String sql = "UPDATE produto SET nome = ?, preco_unitario = ?, unidade = ?, " +
                         "quantidade_estoque = ?, quantidade_minima = ?, quantidade_maxima = ?, " +
                         "categoria_id = ? WHERE id = ?";
            
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
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
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
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para consulta
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, " +
                         "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, " +
                         "c.nome as categoria_nome, c.tamanho as categoria_tamanho, " +
                         "c.embalagem as categoria_embalagem " +
                         "FROM produto p " +
                         "JOIN categoria c ON p.categoria_id = c.id " +
                         "WHERE p.id = ?";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Verifica se encontrou o produto
            if (rs.next()) {
                // Cria a categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));
                
                // Cria o produto
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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para listar todos os produtos
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, " +
                         "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, " +
                         "c.nome as categoria_nome, c.tamanho as categoria_tamanho, " +
                         "c.embalagem as categoria_embalagem " +
                         "FROM produto p " +
                         "JOIN categoria c ON p.categoria_id = c.id " +
                         "ORDER BY p.id";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Percorre os resultados
            while (rs.next()) {
                // Cria a categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));
                
                // Cria o produto
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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para reajuste de preços
            String sql = "UPDATE produto SET preco_unitario = preco_unitario * (1 + ? / 100)";
            
            // Prepara statement para atualização
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, percentual);
            
            // Executa a atualização
            int linhasAfetadas = stmt.executeUpdate();
            
            // Retorna o número de produtos reajustados
            return linhasAfetadas;
        } catch (SQLException ex) {
            System.err.println("Erro ao reajustar preços: " + ex.getMessage());
            return 0;
        } finally {
            // Fecha recursos
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para listar produtos abaixo do mínimo
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, " +
                         "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, " +
                         "c.nome as categoria_nome, c.tamanho as categoria_tamanho, " +
                         "c.embalagem as categoria_embalagem " +
                         "FROM produto p " +
                         "JOIN categoria c ON p.categoria_id = c.id " +
                         "WHERE p.quantidade_estoque < p.quantidade_minima " +
                         "ORDER BY p.nome";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Percorre os resultados
            while (rs.next()) {
                // Cria a categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));
                
                // Cria o produto
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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
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
            conn = ConnectionFactory.getConnection();
            
            // SQL para listar produtos acima do máximo
            String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, " +
                         "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, " +
                         "c.nome as categoria_nome, c.tamanho as categoria_tamanho, " +
                         "c.embalagem as categoria_embalagem " +
                         "FROM produto p " +
                         "JOIN categoria c ON p.categoria_id = c.id " +
                         "WHERE p.quantidade_estoque > p.quantidade_maxima " +
                         "ORDER BY p.nome";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Percorre os resultados
            while (rs.next()) {
                // Cria a categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));
                
                // Cria o produto
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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Lista todos os produtos com os seus preços unitários.
     *
     * @return Uma lista de produtos contendo nome, preço unitário, unidade, quantidade em estoque e categoria.
     */
    public List<Produto> listarComPrecos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, " +
                "c.id as categoria_id, c.nome as categoria_nome, c.tamanho, c.embalagem " +
                "FROM produto p " +
                "JOIN categoria c ON p.categoria_id = c.id " +
                "ORDER BY p.nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("categoria_nome"),
                        rs.getString("tamanho"),
                        rs.getString("embalagem")
                );

                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco_unitario"),
                        rs.getString("unidade"),
                        rs.getInt("quantidade_estoque"),
                        0, 0, // quantidade mínima e máxima não necessárias nesse caso
                        categoria
                );

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório de preços: " + e.getMessage());
        }

        return produtos;
    }

    /**
     * Calcula o valor total do estoque, multiplicando o preço unitário pela quantidade em estoque de cada produto.
     *
     * @return O valor monetário total de todos os produtos em estoque.
     */
    public double calcularValorTotalEstoque() {
        double total = 0;
        String sql = "SELECT SUM(preco_unitario * quantidade_estoque) AS total FROM produto";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular valor total do estoque: " + e.getMessage());
        }

        return total;
    }

    /**
     * Agrupa os produtos cadastrados por categoria.
     *
     * @return Um mapa onde a chave é a categoria e o valor é a lista de produtos pertencentes a ela.
     */
    public Map<Categoria, List<Produto>> listarProdutosPorCategoria() {
        Map<Categoria, List<Produto>> mapa = new HashMap<>();
        String sql = "SELECT p.id, p.nome, p.preco_unitario, p.unidade, p.quantidade_estoque, " +
                "p.quantidade_minima, p.quantidade_maxima, c.id as categoria_id, c.nome as categoria_nome, " +
                "c.tamanho, c.embalagem " +
                "FROM produto p JOIN categoria c ON p.categoria_id = c.id ORDER BY c.nome, p.nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("categoria_nome"),
                        rs.getString("tamanho"),
                        rs.getString("embalagem")
                );

                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco_unitario"),
                        rs.getString("unidade"),
                        rs.getInt("quantidade_estoque"),
                        rs.getInt("quantidade_minima"),
                        rs.getInt("quantidade_maxima"),
                        categoria
                );

                mapa.computeIfAbsent(categoria, k -> new ArrayList<>()).add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos por categoria: " + e.getMessage());
        }

        return mapa;
    }
}
