# Sistema de Gerenciamento de Livraria

Sistema desktop em Java Swing para gerenciar uma livraria com banco de dados MySQL.

## 🚀 Funcionalidades

- **Clientes**: Cadastro de Pessoa Física e Jurídica
- **Livros**: Gestão de estoque e preços
- **Editoras**: Cadastro de editoras
- **Vendas**: Sistema de vendas com controle de estoque
- **Relatórios**: Top 5 livros mais vendidos

## 📋 Pré-requisitos

- Java 8+
- MySQL 5.7+
- Driver JDBC MySQL (`mysql-connector-java-8.0.33.jar`)

## ⚙️ Instalação

1. **Criar banco de dados**:
   ```bash
   mysql -u root -p < schema.sql
   ```

2. **Configurar conexão**:
   ```bash
   cp db.properties.example db.properties
   # Editar db.properties com suas credenciais
   ```

3. **Baixar driver JDBC**:
   - Baixe `mysql-connector-java-8.0.33.jar`
   - Coloque no diretório do projeto

4. **Compilar e executar**:
   ```bash
   ./compile.sh
   ./run.sh
   ```

## 🎯 Como Usar

1. **Cadastre uma editora** (menu Cadastros > Editoras)
2. **Cadastre um cliente** (menu Cadastros > Clientes)
3. **Cadastre livros** (menu Cadastros > Livros)
4. **Faça vendas** (menu Vendas > Nova Venda)
5. **Veja relatórios** (menu Relatórios)

## 📁 Estrutura

```
src/br/uepa/livraria/
├── application/Main.java          # Classe principal
├── model/entities/                # Entidades (Cliente, Livro, etc)
├── model/dao/                     # Interfaces DAO
├── persistence/impl/              # Implementações JDBC
├── db/                           # Conexão com banco
└── ui/                           # Interface gráfica
```

## 🔧 Configuração do Banco

Edite o arquivo `db.properties`:
```properties
dburl=jdbc:mysql://localhost:3306/livraria?useSSL=false&serverTimezone=UTC
user=seu_usuario
password=sua_senha
```

## 📊 Características Técnicas

- **Padrões**: DAO, Factory, Singleton
- **Transações**: Vendas com controle de estoque
- **Validação**: Campos obrigatórios e formatos
- **Interface**: Java Swing com tabelas e formulários

## 🐛 Solução de Problemas

**Erro de conexão**: Verifique se o MySQL está rodando e as credenciais estão corretas
**Driver não encontrado**: Confirme se o JAR está no diretório do projeto
**Erro de compilação**: Verifique se o Java está instalado corretamente
# AtividadePOO2-UEPA
