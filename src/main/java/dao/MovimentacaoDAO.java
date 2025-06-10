package dao;

import model.Movimentacao;
import model.Produto;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados para a entidade Movimentacao.
 * Implementa operações de CRUD (Create, Read, Update, Delete) e consultas específicas,
 * além de atualizar o estoque do produto associado.
 */

public class MovimentacaoDAO {

    // Instância da fábrica de conexões
    private final ConnectionFactory connectionFactory;

    /**
     * Construtor padrão que inicializa a fábrica de conexões.
     */

    public MovimentacaoDAO() {
        this.connectionFactory = new ConnectionFactory();
    }

    /**
     * Insere uma nova movimentação no banco de dados e atualiza o estoque do produto.
     *
     * @param movimentacao A movimentação a ser inserida
     * @return O ID gerado para a movimentação inserida ou -1 em caso de erro
     */

    public int inserir(Movimentacao movimentacao) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idGerado = -1;

        try {
            // Obtém conexão com o banco de dados e inicia transação
            conn = connectionFactory.getConnection();
            conn.setAutoCommit(false);

            // SQL para inserção de movimentação
            String sql = "INSERT INTO movimentacao (produto_id, tipo, quantidade, data_hora, observacao) "
                    + "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, movimentacao.getProduto().getId());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setInt(3, movimentacao.getQuantidade());
            stmt.setTimestamp(4, Timestamp.valueOf(movimentacao.getDataHora()));
            stmt.setString(5, movimentacao.getObservacao());

            // Executa a inserção
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                // Obtém o ID gerado
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    movimentacao.setId(idGerado);

                    // Atualiza o estoque do produto
                    atualizarEstoqueProduto(conn, movimentacao);

                    // Confirma a transação
                    conn.commit();
                }
            } else {
                // Desfaz a transação em caso de falha
                conn.rollback();
            }

            return idGerado;
        } catch (SQLException ex) {
            // Desfaz a transação em caso de erro
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Erro ao desfazer transação: " + rollbackEx.getMessage());
            }
            System.err.println("Erro ao inserir movimentação: " + ex.getMessage());
            return -1;
        } finally {
            // Restaura o modo de auto-commit e fecha recursos
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao restaurar auto-commit: " + ex.getMessage());
            }
            fecharRecursos(rs, stmt, conn);
        }
    }

    /**
     * Atualiza o estoque do produto com base na movimentação.
     *
     * @param conn         A conexão com o banco de dados (dentro da mesma transação)
     * @param movimentacao A movimentação que afeta o estoque
     * @throws SQLException Se ocorrer um erro ao atualizar o estoque
     */

    private void atualizarEstoqueProduto(Connection conn, Movimentacao movimentacao) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql;
            // Se for entrada, aumenta estoque; se for saída, diminui
            if (movimentacao.isEntrada()) {
                sql = "UPDATE produto SET quantidade_estoque = quantidade_estoque + ? WHERE id = ?";
            } else {
                sql = "UPDATE produto SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
            }
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movimentacao.getQuantidade());
            stmt.setInt(2, movimentacao.getProduto().getId());
            stmt.executeUpdate();
        } finally {
            // Fecha apenas o Statement; conexão continua na transação
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
        }
    }

    /**
     * Consulta uma movimentação pelo ID.
     *
     * @param id O ID da movimentação a ser consultada
     * @return A movimentação encontrada ou null se não existir
     */

    public Movimentacao consultar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Movimentacao movimentacao = null;

        try {
            conn = connectionFactory.getConnection();
            String sql = "SELECT m.id, m.produto_id, m.tipo, m.quantidade, m.data_hora, m.observacao, "
                    + "p.nome AS produto_nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM movimentacao m "
                    + "JOIN produto p ON m.produto_id = p.id "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "WHERE m.id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                // Popula Movimentação
                movimentacao = new Movimentacao();
                movimentacao.setId(rs.getInt("id"));
                movimentacao.setProduto(produto);
                movimentacao.setTipo(rs.getString("tipo"));
                movimentacao.setQuantidade(rs.getInt("quantidade"));
                movimentacao.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                movimentacao.setObservacao(rs.getString("observacao"));
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao consultar movimentação: " + ex.getMessage());
        } finally {
            fecharRecursos(rs, stmt, conn);
        }

        return movimentacao;
    }

    /**
     * Lista todas as movimentações cadastradas.
     *
     * @return Uma lista com todas as movimentações
     */

    public List<Movimentacao> listarTodos() {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionFactory.getConnection();
            String sql = "SELECT m.id, m.produto_id, m.tipo, m.quantidade, m.data_hora, m.observacao, "
                    + "p.nome AS produto_nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM movimentacao m "
                    + "JOIN produto p ON m.produto_id = p.id "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "ORDER BY m.id ASC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                // Popula Movimentação
                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setId(rs.getInt("id"));
                movimentacao.setProduto(produto);
                movimentacao.setTipo(rs.getString("tipo"));
                movimentacao.setQuantidade(rs.getInt("quantidade"));
                movimentacao.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                movimentacao.setObservacao(rs.getString("observacao"));

                movimentacoes.add(movimentacao);
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar movimentações: " + ex.getMessage());
        } finally {
            fecharRecursos(rs, stmt, conn);
        }

        return movimentacoes;
    }

    /**
     * Lista as movimentações de um produto específico.
     *
     * @param produtoId O ID do produto
     * @return Uma lista com as movimentações do produto
     */

    public List<Movimentacao> listarPorProduto(int produtoId) {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionFactory.getConnection();
            String sql = "SELECT m.id, m.produto_id, m.tipo, m.quantidade, m.data_hora, m.observacao, "
                    + "p.nome AS produto_nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM movimentacao m "
                    + "JOIN produto p ON m.produto_id = p.id "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "WHERE m.produto_id = ? "
                    + "ORDER BY m.data_hora DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, produtoId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                // Popula Movimentação
                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setId(rs.getInt("id"));
                movimentacao.setProduto(produto);
                movimentacao.setTipo(rs.getString("tipo"));
                movimentacao.setQuantidade(rs.getInt("quantidade"));
                movimentacao.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                movimentacao.setObservacao(rs.getString("observacao"));

                movimentacoes.add(movimentacao);
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar movimentações por produto: " + ex.getMessage());
        } finally {
            fecharRecursos(rs, stmt, conn);
        }

        return movimentacoes;
    }

    /**
     * Lista as movimentações por tipo (Entrada ou Saída).
     *
     * @param tipo O tipo de movimentação ("Entrada" ou "Saída")
     * @return Uma lista com as movimentações do tipo especificado
     */

    public List<Movimentacao> listarPorTipo(String tipo) {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionFactory.getConnection();
            String sql = "SELECT m.id, m.produto_id, m.tipo, m.quantidade, m.data_hora, m.observacao, "
                    + "p.nome AS produto_nome, p.preco_unitario, p.unidade, p.quantidade_estoque, "
                    + "p.quantidade_minima, p.quantidade_maxima, p.categoria_id, "
                    + "c.nome AS categoria_nome, c.tamanho AS categoria_tamanho, "
                    + "c.embalagem AS categoria_embalagem "
                    + "FROM movimentacao m "
                    + "JOIN produto p ON m.produto_id = p.id "
                    + "JOIN categoria c ON p.categoria_id = c.id "
                    + "WHERE m.tipo = ? "
                    + "ORDER BY m.data_hora DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Popula Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));
                categoria.setTamanho(rs.getString("categoria_tamanho"));
                categoria.setEmbalagem(rs.getString("categoria_embalagem"));

                // Popula Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                // Popula Movimentação
                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setId(rs.getInt("id"));
                movimentacao.setProduto(produto);
                movimentacao.setTipo(rs.getString("tipo"));
                movimentacao.setQuantidade(rs.getInt("quantidade"));
                movimentacao.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                movimentacao.setObservacao(rs.getString("observacao"));

                movimentacoes.add(movimentacao);
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar movimentações por tipo: " + ex.getMessage());
        } finally {
            fecharRecursos(rs, stmt, conn);
        }

        return movimentacoes;
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