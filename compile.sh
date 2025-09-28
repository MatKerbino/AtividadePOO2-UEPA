#!/bin/bash

# Script de compilação para o Sistema de Gerenciamento de Livraria
# Certifique-se de ter o driver JDBC do MySQL no diretório

echo "=== Compilando Sistema de Gerenciamento de Livraria ==="

# Verifica se o driver JDBC existe
if [ ! -f "mysql-connector-j-9.3.0.jar" ]; then
    echo "ERRO: Driver JDBC do MySQL não encontrado!"
    echo "Baixe o mysql-connector-j-9.3.0.jar e coloque no diretório do projeto."
    exit 1
fi

# Cria diretório de classes se não existir
mkdir -p classes

# Compila todos os arquivos Java
echo "Compilando arquivos Java..."
javac -cp ".:mysql-connector-j-9.3.0.jar" -d classes $(find src/br/uepa/livraria -name "*.java")

if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
    echo ""
    echo "Para executar a aplicação, use:"
    echo "java -cp \"classes:mysql-connector-j-9.3.0.jar\" br.uepa.livraria.application.Main"
    echo ""
    echo "Ou execute: ./run.sh"
    echo ""
    echo "Deseja criar o banco de dados e tabelas? (schema.sql)"
    read -p "Digite o usuário do MySQL (ex: root): " MYSQL_USER
    echo "Será solicitado a senha do MySQL."
    mysql -u "$MYSQL_USER" -p < schema.sql
    if [ $? -eq 0 ]; then
        echo "Banco de dados criado/configurado com sucesso!"
    else
        echo "ERRO ao executar o schema.sql! Verifique usuário, senha e permissões."
    fi
else
    echo "ERRO: Falha na compilação!"
    exit 1
fi
