package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;

/**
 * Classe de acesso a dados para a entidade Categoria.
 * Implementa operações de CRUD (Create, Read, Update, Delete) e consultas específicas.
 */
public class CategoriaDAO {
    
    /**
     * Insere uma nova categoria no banco de dados.
     * 
     * @param categoria A categoria a ser inserida
     * @return O ID gerado para a categoria inserida ou -1 em caso de erro
     */
    public int inserir(Categoria categoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idGerado = -1;
        
        try {
            // Obtém conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            // SQL para inserção
            String sql = "INSERT INTO categoria (nome, tamanho, embalagem) VALUES (?, ?, ?)";
            
            // Prepara statement para inserção
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTamanho());
            stmt.setString(3, categoria.getEmbalagem());
            
            // Executa a inserção
            int linhasAfetadas = stmt.executeUpdate();
            
            // Verifica se a inserção foi bem-sucedida
            if (linhasAfetadas > 0) {
                // Obtém o ID gerado
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    categoria.setId(idGerado);
                }
            }
            
            return idGerado;
        } catch (SQLException ex) {
            System.err.println("Erro ao inserir categoria: " + ex.getMessage());
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
     * Atualiza uma categoria existente no banco de dados.
     * 
     * @param categoria A categoria a ser atualizada
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Categoria categoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Obtém conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            // SQL para atualização
            String sql = "UPDATE categoria SET nome = ?, tamanho = ?, embalagem = ? WHERE id = ?";
            
            // Prepara statement para atualização
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTamanho());
            stmt.setString(3, categoria.getEmbalagem());
            stmt.setInt(4, categoria.getId());
            
            // Executa a atualização
            int linhasAfetadas = stmt.executeUpdate();
            
            // Verifica se a atualização foi bem-sucedida
            return linhasAfetadas > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar categoria: " + ex.getMessage());
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
     * Exclui uma categoria do banco de dados.
     * 
     * @param id O ID da categoria a ser excluída
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Obtém conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            // SQL para exclusão
            String sql = "DELETE FROM categoria WHERE id = ?";
            
            // Prepara statement para exclusão
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            // Executa a exclusão
            int linhasAfetadas = stmt.executeUpdate();
            
            // Verifica se a exclusão foi bem-sucedida
            return linhasAfetadas > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao excluir categoria: " + ex.getMessage());
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
     * Consulta uma categoria pelo ID.
     * 
     * @param id O ID da categoria a ser consultada
     * @return A categoria encontrada ou null se não existir
     */
    public Categoria consultar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Categoria categoria = null;
        
        try {
            // Obtém conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            // SQL para consulta
            String sql = "SELECT id, nome, tamanho, embalagem FROM categoria WHERE id = ?";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Verifica se encontrou a categoria
            if (rs.next()) {
                categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setTamanho(rs.getString("tamanho"));
                categoria.setEmbalagem(rs.getString("embalagem"));
            }
            
            return categoria;
        } catch (SQLException ex) {
            System.err.println("Erro ao consultar categoria: " + ex.getMessage());
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
     * Lista todas as categorias cadastradas.
     * 
     * @return Uma lista com todas as categorias
     */
    public List<Categoria> listarTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Categoria> categorias = new ArrayList<>();
        
        try {
            // Obtém conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            // SQL para listar todas as categorias
            String sql = "SELECT id, nome, tamanho, embalagem FROM categoria ORDER BY id";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Percorre os resultados
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setTamanho(rs.getString("tamanho"));
                categoria.setEmbalagem(rs.getString("embalagem"));
                
                categorias.add(categoria);
            }
            
            return categorias;
        } catch (SQLException ex) {
            System.err.println("Erro ao listar categorias: " + ex.getMessage());
            return categorias;
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
     * Conta a quantidade de produtos por categoria.
     * 
     * @return Uma lista de arrays com [id_categoria, nome_categoria, quantidade]
     */
    public List<Object[]> contarProdutosPorCategoria() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object[]> resultado = new ArrayList<>();
        
        try {
            // Obtém conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            
            // SQL para contar produtos por categoria
            String sql = "SELECT c.id, c.nome, COUNT(p.id) as quantidade " +
                         "FROM categoria c " +
                         "LEFT JOIN produto p ON c.id = p.categoria_id " +
                         "GROUP BY c.id, c.nome " +
                         "ORDER BY c.nome";
            
            // Prepara statement para consulta
            stmt = conn.prepareStatement(sql);
            
            // Executa a consulta
            rs = stmt.executeQuery();
            
            // Percorre os resultados
            while (rs.next()) {
                Object[] linha = new Object[3];
                linha[0] = rs.getInt("id");
                linha[1] = rs.getString("nome");
                linha[2] = rs.getInt("quantidade");
                
                resultado.add(linha);
            }
            
            return resultado;
        } catch (SQLException ex) {
            System.err.println("Erro ao contar produtos por categoria: " + ex.getMessage());
            return resultado;
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
}
