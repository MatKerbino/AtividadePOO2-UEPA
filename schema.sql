-- Script SQL para criação do banco de dados da livraria
-- Execute este script no MySQL para criar as tabelas necessárias

CREATE DATABASE IF NOT EXISTS livraria;
USE livraria;

-- Tabela Editora
CREATE TABLE IF NOT EXISTS Editora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Tabela Cliente (suporta Pessoa Física e Jurídica)
CREATE TABLE IF NOT EXISTS Cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    tipo_cliente ENUM('PF', 'PJ') NOT NULL,
    cpf VARCHAR(14) NULL,
    cnpj VARCHAR(18) NULL,
    razao_social VARCHAR(100) NULL
);

-- Tabela Livro
CREATE TABLE IF NOT EXISTS Livro (
    isbn VARCHAR(20) PRIMARY KEY,
    titulo_autor VARCHAR(200) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    ano_publicacao INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL DEFAULT 0,
    id_editora INT NOT NULL,
    FOREIGN KEY (id_editora) REFERENCES Editora(id)
);

-- Tabela Compra (Vendas)
CREATE TABLE IF NOT EXISTS Compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn_livro VARCHAR(20) NOT NULL,
    id_cliente INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    data_compra TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (isbn_livro) REFERENCES Livro(isbn),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id)
);

-- Inserção de dados de exemplo

-- Editoras
INSERT INTO Editora (nome, endereco, telefone, email) VALUES
('Editora Saraiva', 'Rua das Flores, 123, São Paulo - SP', '(11) 3333-4444', 'contato@saraiva.com.br'),
('Editora Globo', 'Av. Paulista, 1000, São Paulo - SP', '(11) 2222-3333', 'contato@globo.com.br'),
('Editora Abril', 'Rua da Consolação, 500, São Paulo - SP', '(11) 1111-2222', 'contato@abril.com.br');

-- Clientes Pessoa Física
INSERT INTO Cliente (nome, endereco, telefone, email, tipo_cliente, cpf) VALUES
('João Silva', 'Rua A, 100, Belém - PA', '(91) 99999-1111', 'joao@email.com', 'PF', '123.456.789-00'),
('Maria Santos', 'Rua B, 200, Belém - PA', '(91) 99999-2222', 'maria@email.com', 'PF', '987.654.321-00'),
('Pedro Oliveira', 'Rua C, 300, Belém - PA', '(91) 99999-3333', 'pedro@email.com', 'PF', '456.789.123-00');

-- Clientes Pessoa Jurídica
INSERT INTO Cliente (nome, endereco, telefone, email, tipo_cliente, cnpj, razao_social) VALUES
('Livraria Central LTDA', 'Av. Presidente Vargas, 500, Belém - PA', '(91) 3333-1111', 'contato@livrariacentral.com.br', 'PJ', '12.345.678/0001-90', 'Livraria Central LTDA'),
('Empresa ABC LTDA', 'Rua Comercial, 100, Belém - PA', '(91) 3333-2222', 'contato@empresaabc.com.br', 'PJ', '98.765.432/0001-10', 'Empresa ABC LTDA');

-- Livros
INSERT INTO Livro (isbn, titulo_autor, genero, ano_publicacao, preco, estoque, id_editora) VALUES
('978-85-221-1234-5', 'Dom Casmurro - Machado de Assis', 'Romance', 1899, 25.90, 50, 1),
('978-85-221-2345-6', 'O Cortiço - Aluísio Azevedo', 'Romance', 1890, 22.50, 30, 1),
('978-85-221-3456-7', 'Capitães da Areia - Jorge Amado', 'Romance', 1937, 28.90, 40, 2),
('978-85-221-4567-8', 'O Guarani - José de Alencar', 'Romance', 1857, 20.90, 25, 1),
('978-85-221-5678-9', 'Iracema - José de Alencar', 'Romance', 1865, 19.90, 35, 1),
('978-85-221-6789-0', 'Memórias Póstumas de Brás Cubas - Machado de Assis', 'Romance', 1881, 26.90, 45, 1),
('978-85-221-7890-1', 'O Primo Basílio - Eça de Queirós', 'Romance', 1878, 24.90, 20, 2),
('978-85-221-8901-2', 'A Moreninha - Joaquim Manuel de Macedo', 'Romance', 1844, 18.90, 15, 1),
('978-85-221-9012-3', 'O Mulato - Aluísio Azevedo', 'Romance', 1881, 21.90, 30, 1),
('978-85-221-0123-4', 'A Escrava Isaura - Bernardo Guimarães', 'Romance', 1875, 23.90, 25, 2);

-- Compras de exemplo
INSERT INTO Compra (isbn_livro, id_cliente, quantidade, preco_unitario, data_compra) VALUES
('978-85-221-1234-5', 1, 2, 25.90, '2024-01-15 10:30:00'),
('978-85-221-2345-6', 1, 1, 22.50, '2024-01-15 10:35:00'),
('978-85-221-3456-7', 2, 3, 28.90, '2024-01-16 14:20:00'),
('978-85-221-1234-5', 3, 1, 25.90, '2024-01-17 09:15:00'),
('978-85-221-4567-8', 4, 5, 20.90, '2024-01-18 16:45:00'),
('978-85-221-5678-9', 2, 2, 19.90, '2024-01-19 11:30:00'),
('978-85-221-1234-5', 5, 1, 25.90, '2024-01-20 13:20:00'),
('978-85-221-6789-0', 1, 1, 26.90, '2024-01-21 15:10:00'),
('978-85-221-3456-7', 3, 2, 28.90, '2024-01-22 10:45:00'),
('978-85-221-1234-5', 4, 3, 25.90, '2024-01-23 14:30:00');

-- Atualiza o estoque dos livros baseado nas vendas
UPDATE Livro SET estoque = estoque - 2 WHERE isbn = '978-85-221-1234-5';
UPDATE Livro SET estoque = estoque - 1 WHERE isbn = '978-85-221-2345-6';
UPDATE Livro SET estoque = estoque - 3 WHERE isbn = '978-85-221-3456-7';
UPDATE Livro SET estoque = estoque - 1 WHERE isbn = '978-85-221-1234-5';
UPDATE Livro SET estoque = estoque - 5 WHERE isbn = '978-85-221-4567-8';
UPDATE Livro SET estoque = estoque - 2 WHERE isbn = '978-85-221-5678-9';
UPDATE Livro SET estoque = estoque - 1 WHERE isbn = '978-85-221-1234-5';
UPDATE Livro SET estoque = estoque - 1 WHERE isbn = '978-85-221-6789-0';
UPDATE Livro SET estoque = estoque - 2 WHERE isbn = '978-85-221-3456-7';
UPDATE Livro SET estoque = estoque - 3 WHERE isbn = '978-85-221-1234-5';

-- Verifica os dados inseridos
SELECT 'Editoras:' as Tabela, COUNT(*) as Total FROM Editora
UNION ALL
SELECT 'Clientes:', COUNT(*) FROM Cliente
UNION ALL
SELECT 'Livros:', COUNT(*) FROM Livro
UNION ALL
SELECT 'Compras:', COUNT(*) FROM Compra;
