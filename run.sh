#!/bin/bash

# Script de execução para o Sistema de Gerenciamento de Livraria

echo "=== Iniciando Sistema de Gerenciamento de Livraria ==="

# Verifica se o driver JDBC existe
if [ ! -f "mysql-connector-j-9.3.0.jar" ]; then
    echo "ERRO: Driver JDBC do MySQL não encontrado!"
    echo "Baixe o mysql-connector-j-9.3.0.jar e coloque no diretório do projeto."
    exit 1
fi

# Verifica se as classes foram compiladas
if [ ! -d "classes" ]; then
    echo "Classes não compiladas. Executando compilação..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        echo "ERRO: Falha na compilação!"
        exit 1
    fi
fi

# Executa a aplicação
echo "Iniciando aplicação..."
java -cp "classes:mysql-connector-j-9.3.0.jar" br.uepa.livraria.application.Main
