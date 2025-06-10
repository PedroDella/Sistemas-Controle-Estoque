package util;

/**
 * Classe utilitária para validação de dados de entrada.
 * Contém métodos para validar diferentes tipos de dados.
 */

public class Validador {
    
    /**
     * Verifica se uma string não é nula e não está vazia.
     * 
     * @param texto O texto a ser validado
     * @return true se o texto for válido, false caso contrário
     */

    public static boolean validarTexto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Verifica se um número é positivo.
     * 
     * @param numero O número a ser validado
     * @return true se o número for positivo, false caso contrário
     */

    public static boolean validarPositivo(double numero) {
        return numero > 0;
    }
    
    /**
     * Verifica se um número é não-negativo (zero ou positivo).
     * 
     * @param numero O número a ser validado
     * @return true se o número for não-negativo, false caso contrário
     */

    public static boolean validarNaoNegativo(double numero) {
        return numero >= 0;
    }
    
    /**
     * Verifica se um número está dentro de um intervalo.
     * 
     * @param numero O número a ser validado
     * @param minimo O valor mínimo do intervalo
     * @param maximo O valor máximo do intervalo
     * @return true se o número estiver dentro do intervalo, false caso contrário
     */

    public static boolean validarIntervalo(double numero, double minimo, double maximo) {
        return numero >= minimo && numero <= maximo;
    }
    
    /**
     * Verifica se uma quantidade é válida para movimentação de estoque.
     * 
     * @param quantidade A quantidade a ser validada
     * @return true se a quantidade for válida, false caso contrário
     */

    public static boolean validarQuantidade(int quantidade) {
        return quantidade > 0;
    }
    
    /**
     * Verifica se uma quantidade é válida para saída de estoque.
     * 
     * @param quantidade A quantidade a ser retirada
     * @param estoqueAtual A quantidade atual em estoque
     * @return true se a quantidade for válida para saída, false caso contrário
     */

    public static boolean validarQuantidadeSaida(int quantidade, int estoqueAtual) {
        return validarQuantidade(quantidade) && quantidade <= estoqueAtual;
    }
    
    /**
     * Verifica se um percentual de reajuste é válido.
     * 
     * @param percentual O percentual a ser validado
     * @return true se o percentual for válido, false caso contrário
     */

    public static boolean validarPercentual(double percentual) {
        return percentual != 0;
    }
}