# Estrutura do Projeto - Sistema de Controle de Estoque

## Estrutura de Pacotes

```
src/main/java/
├── db/
│   └── estoque.sql
├── model/
│   ├── Produto.java
│   ├── Categoria.java
│   └── Movimentacao.java
├── dao/
│   ├── ConnectionFactory.java
│   ├── ProdutoDAO.java
│   ├── CategoriaDAO.java
│   └── MovimentacaoDAO.java
├── view/
│   ├── App.java (Classe principal)
│   ├── TelaPrincipal.java
│   ├── TelaSobre.java
│   ├── produto/
│   │   ├── CadastroProdutoView.java
│   │   ├── ConsultarProdutoView.java
│   │   ├── ReajustarPrecoProdutoUnitarioView.java
│   │   └── ReajustarPrecoProdutoView.java
│   ├── categoria/
│   │   ├── CadastrarCategoriaView.java
│   │   └── ConsultarCategoriasView.java
│   ├── movimentacao/
│   │   ├── EntradaEstoqueView.java
│   │   ├── SaidaEstoqueView.java
│   │   └── HistoricoEstoqueView.java
│   └── relatorio/
│       ├── RelatorioPrecosView.java
│       ├── RelatorioBalancoView.java
│       ├── RelatorioAbaixoMinimoView.java
│       ├── RelatorioAcimaMaximoView.java
│       └── RelatorioPorCategoriaView.java
└── util/
    ├── Validador.java
    └── GeradorRelatorio.java
```

## Banco de Dados

```sql
-- Estrutura do banco de dados
CREATE DATABASE IF NOT EXISTS controle_estoque;
USE controle_estoque;

-- Tabela de Categorias
CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tamanho ENUM('Pequeno', 'Médio', 'Grande') NOT NULL,
    embalagem ENUM('Lata', 'Vidro', 'Plástico') NOT NULL
);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    unidade VARCHAR(20) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    quantidade_minima INT NOT NULL,
    quantidade_maxima INT NOT NULL,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Tabela de Movimentações
CREATE TABLE IF NOT EXISTS movimentacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    tipo ENUM('Entrada', 'Saída') NOT NULL,
    quantidade INT NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);
```

## Classes Principais

### Model

#### Produto
- Atributos: id, nome, precoUnitario, unidade, quantidadeEstoque, quantidadeMinima, quantidadeMaxima, categoria
- Métodos: getters, setters, toString, equals, hashCode

#### Categoria
- Atributos: id, nome, tamanho, embalagem
- Métodos: getters, setters, toString, equals, hashCode

#### Movimentacao
- Atributos: id, produto, tipo, quantidade, dataHora, observacao
- Métodos: getters, setters, toString

### DAO

#### ConnectionFactory
- Responsável por criar e gerenciar conexões com o banco de dados
- Métodos: getConnection, closeConnection

#### ProdutoDAO
- Métodos CRUD: inserir, atualizar, excluir, consultar, listarTodos
- Métodos específicos: reajustarPrecos, listarAbaixoMinimo, listarAcimaMaximo

#### CategoriaDAO
- Métodos CRUD: inserir, atualizar, excluir, consultar, listarTodos
- Métodos específicos: contarProdutosPorCategoria

#### MovimentacaoDAO
- Métodos CRUD: inserir, atualizar, excluir, consultar, listarTodos
- Métodos específicos: listarPorProduto, listarPorTipo, listarPorPeriodo

### View

#### App
- Classe principal que inicia a aplicação
- Método main

#### TelaPrincipal
- Interface principal com menu de acesso às funcionalidades
- Métodos para abrir as telas específicas

#### Telas de Produto, Categoria e Movimentação
- Interfaces para operações CRUD
- Validação de entrada de dados
- Comunicação com as classes DAO

#### Telas de Relatório
- Interfaces para geração e visualização de relatórios
- Comunicação com as classes DAO para obtenção dos dados

### Util

#### Validador
- Métodos para validação de entrada de dados
- Validação de campos obrigatórios, formatos, etc.

#### GeradorRelatorio
- Métodos para geração de relatórios
- Formatação e apresentação dos dados

## Padrões e Boas Práticas

1. **Padrão DAO**: Separação clara entre lógica de negócio e acesso a dados
2. **MVC**: Separação em Model (modelo), View (visualização) e Controller (embutido nas classes DAO)
3. **Clean Code**: 
   - Nomes significativos em português
   - Métodos pequenos e com responsabilidade única
   - Comentários explicativos
   - Tratamento adequado de exceções
4. **Encapsulamento**: Atributos privados com getters e setters
5. **Validação**: Validação de dados antes de persistência
6. **Tratamento de Exceções**: Captura e tratamento adequado de exceções