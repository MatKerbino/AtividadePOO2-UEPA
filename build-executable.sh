#!/bin/bash

echo "=== Criando Executável do Sistema de Livraria ==="

# Verifica se o Java 14+ está disponível
if ! java -version 2>&1 | grep -q "version \"1[4-9]\|version \"[2-9]"; then
    echo "ERRO: Java 14+ é necessário para jpackage!"
    echo "Instale o OpenJDK 14 ou superior."
    exit 1
fi

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

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação!"
    exit 1
fi

echo "Compilação concluída com sucesso!"

# Cria o arquivo JAR
echo "Criando arquivo JAR..."
cd classes
jar cfe ../livraria.jar br.uepa.livraria.application.Main br/uepa/livraria/
cd ..

# Cria diretório de distribuição
mkdir -p dist

# Copia arquivos necessários
cp livraria.jar dist/
cp mysql-connector-j-9.3.0.jar dist/
cp db.properties dist/
cp schema.sql dist/

# Detecta o sistema operacional
OS=$(uname -s)
case $OS in
    Linux*)
        echo "Criando executável para Linux..."
        jpackage --input dist \
                 --main-jar livraria.jar \
                 --main-class br.uepa.livraria.application.Main \
                 --name "SistemaLivraria" \
                 --app-version "1.0" \
                 --description "Sistema de Gerenciamento de Livraria" \
                 --vendor "UEPA" \
                 --type app-image \
                 --add-modules java.desktop,java.sql
        ;;
    Darwin*)
        echo "Criando executável para macOS..."
        jpackage --input dist \
                 --main-jar livraria.jar \
                 --main-class br.uepa.livraria.application.Main \
                 --name "SistemaLivraria" \
                 --app-version "1.0" \
                 --description "Sistema de Gerenciamento de Livraria" \
                 --vendor "UEPA" \
                 --type app-image \
                 --add-modules java.desktop,java.sql
        ;;
    CYGWIN*|MINGW*|MSYS*)
        echo "Criando executável para Windows..."
        jpackage --input dist \
                 --main-jar livraria.jar \
                 --main-class br.uepa.livraria.application.Main \
                 --name "SistemaLivraria" \
                 --app-version "1.0" \
                 --description "Sistema de Gerenciamento de Livraria" \
                 --vendor "UEPA" \
                 --type exe \
                 --add-modules java.desktop,java.sql
        ;;
    *)
        echo "Sistema operacional não suportado: $OS"
        exit 1
        ;;
esac

if [ $? -eq 0 ]; then
    echo ""
    echo "=== Executável criado com sucesso! ==="
    echo ""
    case $OS in
        Linux*|Darwin*)
            echo "Executável criado em: SistemaLivraria/"
            echo "Para executar: ./SistemaLivraria/bin/SistemaLivraria"
            ;;
        CYGWIN*|MINGW*|MSYS*)
            echo "Executável criado: SistemaLivraria-1.0.exe"
            echo "Para executar: SistemaLivraria-1.0.exe"
            ;;
    esac
else
    echo "ERRO: Falha ao criar executável!"
    echo "Verifique se o jpackage está disponível e se todos os módulos estão instalados."
fi
