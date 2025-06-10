package util;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import model.Categoria;
import model.Produto;

/**
 * Classe utilitária para geração de relatórios do sistema.
 * Contém métodos para gerar diferentes tipos de relatórios.
 */

public class GeradorRelatorio {
    
    /**
     * Gera um relatório de lista de preços.
     * 
     * @param produtos Lista de produtos para o relatório
     * @param caminhoArquivo Caminho do arquivo onde o relatório será salvo
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */

    public static boolean gerarRelatorioPrecos(List<Produto> produtos, String caminhoArquivo) {
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            StringBuilder relatorio = new StringBuilder();
            
            // Cabeçalho do relatório
            relatorio.append("LISTA DE PREÇOS\n");
            relatorio.append("Data: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n\n");
            relatorio.append(String.format("%-40s %-10s %-15s %-20s\n", "PRODUTO", "UNIDADE", "PREÇO", "CATEGORIA"));
            relatorio.append("--------------------------------------------------------------------------------\n");
            
            // Dados dos produtos
            for (Produto produto : produtos) {
                relatorio.append(String.format("%-40s %-10s R$ %-12.2f %-20s\n",
                        produto.getNome(),
                        produto.getUnidade(),
                        produto.getPrecoUnitario(),
                        produto.getCategoria().getNome()));
            }
            
            // Rodapé do relatório
            relatorio.append("--------------------------------------------------------------------------------\n");
            relatorio.append("Total de produtos: ").append(produtos.size()).append("\n");
            
            // Escreve o relatório no arquivo
            fos.write(relatorio.toString().getBytes());
            
            return true;
        } catch (Exception ex) {
            System.err.println("Erro ao gerar relatório de preços: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Gera um relatório de balanço físico/financeiro.
     * 
     * @param produtos Lista de produtos para o relatório
     * @param caminhoArquivo Caminho do arquivo onde o relatório será salvo
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */

    public static boolean gerarRelatorioBalanco(List<Produto> produtos, String caminhoArquivo) {
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            StringBuilder relatorio = new StringBuilder();
            double valorTotalEstoque = 0;
            
            // Cabeçalho do relatório
            relatorio.append("BALANÇO FÍSICO/FINANCEIRO\n");
            relatorio.append("Data: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n\n");
            relatorio.append(String.format("%-40s %-10s %-15s %-15s %-15s\n", 
                    "PRODUTO", "UNIDADE", "QUANTIDADE", "PREÇO UNIT.", "VALOR TOTAL"));
            relatorio.append("--------------------------------------------------------------------------------\n");
            
            // Dados dos produtos
            for (Produto produto : produtos) {
                double valorTotal = produto.getQuantidadeEstoque() * produto.getPrecoUnitario();
                valorTotalEstoque += valorTotal;
                
                relatorio.append(String.format("%-40s %-10s %-15d R$ %-12.2f R$ %-12.2f\n",
                        produto.getNome(),
                        produto.getUnidade(),
                        produto.getQuantidadeEstoque(),
                        produto.getPrecoUnitario(),
                        valorTotal));
            }
            
            // Rodapé do relatório
            relatorio.append("--------------------------------------------------------------------------------\n");
            relatorio.append("Total de produtos: ").append(produtos.size()).append("\n");
            relatorio.append("Valor total em estoque: R$ ").append(String.format("%.2f", valorTotalEstoque)).append("\n");
            
            // Escreve o relatório no arquivo
            fos.write(relatorio.toString().getBytes());
            
            return true;
        } catch (Exception ex) {
            System.err.println("Erro ao gerar relatório de balanço: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Gera um relatório de produtos abaixo da quantidade mínima.
     * 
     * @param produtos Lista de produtos para o relatório
     * @param caminhoArquivo Caminho do arquivo onde o relatório será salvo
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */

    public static boolean gerarRelatorioAbaixoMinimo(List<Produto> produtos, String caminhoArquivo) {
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            StringBuilder relatorio = new StringBuilder();
            
            // Cabeçalho do relatório
            relatorio.append("PRODUTOS ABAIXO DA QUANTIDADE MÍNIMA\n");
            relatorio.append("Data: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n\n");
            relatorio.append(String.format("%-40s %-15s %-15s %-15s\n", 
                    "PRODUTO", "QTD. MÍNIMA", "QTD. ATUAL", "DIFERENÇA"));
            relatorio.append("--------------------------------------------------------------------------------\n");
            
            // Filtra produtos abaixo do mínimo
            List<Produto> produtosAbaixoMinimo = produtos.stream()
                    .filter(p -> p.getQuantidadeEstoque() < p.getQuantidadeMinima())
                    .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                    .collect(Collectors.toList());
            
            // Dados dos produtos
            for (Produto produto : produtosAbaixoMinimo) {
                int diferenca = produto.getQuantidadeMinima() - produto.getQuantidadeEstoque();
                
                relatorio.append(String.format("%-40s %-15d %-15d %-15d\n",
                        produto.getNome(),
                        produto.getQuantidadeMinima(),
                        produto.getQuantidadeEstoque(),
                        diferenca));
            }
            
            // Rodapé do relatório
            relatorio.append("--------------------------------------------------------------------------------\n");
            relatorio.append("Total de produtos abaixo do mínimo: ").append(produtosAbaixoMinimo.size()).append("\n");
            
            // Escreve o relatório no arquivo
            fos.write(relatorio.toString().getBytes());
            
            return true;
        } catch (Exception ex) {
            System.err.println("Erro ao gerar relatório de produtos abaixo do mínimo: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Gera um relatório de produtos acima da quantidade máxima.
     * 
     * @param produtos Lista de produtos para o relatório
     * @param caminhoArquivo Caminho do arquivo onde o relatório será salvo
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */

    public static boolean gerarRelatorioAcimaMaximo(List<Produto> produtos, String caminhoArquivo) {
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            StringBuilder relatorio = new StringBuilder();
            
            // Cabeçalho do relatório
            relatorio.append("PRODUTOS ACIMA DA QUANTIDADE MÁXIMA\n");
            relatorio.append("Data: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n\n");
            relatorio.append(String.format("%-40s %-15s %-15s %-15s\n", 
                    "PRODUTO", "QTD. MÁXIMA", "QTD. ATUAL", "DIFERENÇA"));
            relatorio.append("--------------------------------------------------------------------------------\n");
            
            // Filtra produtos acima do máximo
            List<Produto> produtosAcimaMaximo = produtos.stream()
                    .filter(p -> p.getQuantidadeEstoque() > p.getQuantidadeMaxima())
                    .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                    .collect(Collectors.toList());
            
            // Dados dos produtos
            for (Produto produto : produtosAcimaMaximo) {
                int diferenca = produto.getQuantidadeEstoque() - produto.getQuantidadeMaxima();
                
                relatorio.append(String.format("%-40s %-15d %-15d %-15d\n",
                        produto.getNome(),
                        produto.getQuantidadeMaxima(),
                        produto.getQuantidadeEstoque(),
                        diferenca));
            }
            
            // Rodapé do relatório
            relatorio.append("--------------------------------------------------------------------------------\n");
            relatorio.append("Total de produtos acima do máximo: ").append(produtosAcimaMaximo.size()).append("\n");
            
            // Escreve o relatório no arquivo
            fos.write(relatorio.toString().getBytes());
            
            return true;
        } catch (Exception ex) {
            System.err.println("Erro ao gerar relatório de produtos acima do máximo: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Gera um relatório de quantidade de produtos por categoria.
     * 
     * @param categorias Lista de categorias para o relatório
     * @param dadosProdutosPorCategoria Lista de arrays com [id_categoria, nome_categoria, quantidade]
     * @param caminhoArquivo Caminho do arquivo onde o relatório será salvo
     * @return true se o relatório foi gerado com sucesso, false caso contrário
     */

    public static boolean gerarRelatorioPorCategoria(List<Categoria> categorias, 
                                                    List<Object[]> dadosProdutosPorCategoria, 
                                                    String caminhoArquivo) {
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            StringBuilder relatorio = new StringBuilder();
            int totalProdutos = 0;
            
            // Cabeçalho do relatório
            relatorio.append("QUANTIDADE DE PRODUTOS POR CATEGORIA\n");
            relatorio.append("Data: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n\n");
            relatorio.append(String.format("%-40s %-15s %-20s\n", 
                    "CATEGORIA", "QUANTIDADE", "CARACTERÍSTICAS"));
            relatorio.append("--------------------------------------------------------------------------------\n");
            
            // Dados das categorias
            for (Object[] dados : dadosProdutosPorCategoria) {
                String nomeCategoria = (String) dados[1];
                int quantidade = (int) dados[2];
                totalProdutos += quantidade;
                
                // Busca características da categoria
                String caracteristicas = "";
                for (Categoria categoria : categorias) {
                    if (categoria.getNome().equals(nomeCategoria)) {
                        caracteristicas = categoria.getTamanho() + ", " + categoria.getEmbalagem();
                        break;
                    }
                }
                
                relatorio.append(String.format("%-40s %-15d %-20s\n",
                        nomeCategoria,
                        quantidade,
                        caracteristicas));
            }
            
            // Rodapé do relatório
            relatorio.append("--------------------------------------------------------------------------------\n");
            relatorio.append("Total de categorias: ").append(dadosProdutosPorCategoria.size()).append("\n");
            relatorio.append("Total de produtos: ").append(totalProdutos).append("\n");
            
            // Escreve o relatório no arquivo
            fos.write(relatorio.toString().getBytes());
            
            return true;
        } catch (Exception ex) {
            System.err.println("Erro ao gerar relatório por categoria: " + ex.getMessage());
            return false;
        }
    }
}