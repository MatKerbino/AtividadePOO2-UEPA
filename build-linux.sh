#!/bin/bash

echo "=== Compilando Sistema de Gerenciamento de Livraria para Linux ==="

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

# Cria diretório de distribuição
mkdir -p dist

# Copia arquivos necessários
cp mysql-connector-j-9.3.0.jar dist/
cp db.properties dist/
cp schema.sql dist/

# Cria o arquivo JAR executável
echo "Criando arquivo JAR executável..."
cd classes
jar cfe ../dist/livraria.jar br.uepa.livraria.application.Main br/uepa/livraria/
cd ..

# Cria o script de execução
cat > dist/run-livraria.sh << 'EOF'
#!/bin/bash
cd "$(dirname "$0")"
java -cp "livraria.jar:mysql-connector-j-9.3.0.jar" br.uepa.livraria.application.Main
EOF

chmod +x dist/run-livraria.sh

# Cria um AppImage básico (requer AppImageKit)
if command -v appimagetool &> /dev/null; then
    echo "Criando AppImage..."
    
    # Cria estrutura do AppImage
    mkdir -p dist/Livraria.AppDir/usr/bin
    mkdir -p dist/Livraria.AppDir/usr/lib
    mkdir -p dist/Livraria.AppDir/usr/share/applications
    mkdir -p dist/Livraria.AppDir/usr/share/icons
    
    # Copia arquivos
    cp dist/livraria.jar dist/Livraria.AppDir/usr/bin/
    cp dist/mysql-connector-j-9.3.0.jar dist/Livraria.AppDir/usr/lib/
    cp dist/db.properties dist/Livraria.AppDir/usr/bin/
    cp dist/schema.sql dist/Livraria.AppDir/usr/bin/
    
    # Cria script de execução
    cat > dist/Livraria.AppDir/AppRun << 'EOF'
#!/bin/bash
cd "$(dirname "$0")"
exec java -cp "usr/bin/livraria.jar:usr/lib/mysql-connector-j-9.3.0.jar" br.uepa.livraria.application.Main
EOF
    chmod +x dist/Livraria.AppDir/AppRun
    
    # Cria arquivo .desktop
    cat > dist/Livraria.AppDir/livraria.desktop << 'EOF'
[Desktop Entry]
Name=Sistema de Livraria
Comment=Sistema de Gerenciamento de Livraria
Exec=livraria
Icon=livraria
Type=Application
Categories=Office;
EOF
    
    # Cria ícone (se não existir)
    if [ ! -f "dist/Livraria.AppDir/livraria.png" ]; then
        # Cria um ícone simples usando ImageMagick se disponível
        if command -v convert &> /dev/null; then
            convert -size 64x64 xc:blue -fill white -pointsize 20 -gravity center -annotate +0+0 "L" dist/Livraria.AppDir/livraria.png
        else
            # Cria um arquivo vazio como fallback
            touch dist/Livraria.AppDir/livraria.png
        fi
    fi
    
    # Gera o AppImage
    appimagetool dist/Livraria.AppDir dist/Livraria-x86_64.AppImage
    
    if [ $? -eq 0 ]; then
        echo "AppImage criado com sucesso: dist/Livraria-x86_64.AppImage"
    else
        echo "Aviso: Falha ao criar AppImage. Use o script run-livraria.sh"
    fi
else
    echo "Aviso: appimagetool não encontrado. AppImage não será criado."
    echo "Para instalar: sudo apt install appimagetool (Ubuntu/Debian)"
fi

echo ""
echo "=== Build concluído com sucesso! ==="
echo ""
echo "Arquivos gerados em: dist/"
echo "- livraria.jar (executável principal)"
echo "- run-livraria.sh (script para executar)"
echo "- mysql-connector-j-9.3.0.jar (driver do banco)"
echo "- db.properties (configuração do banco)"
echo "- schema.sql (script do banco)"
if [ -f "dist/Livraria-x86_64.AppImage" ]; then
    echo "- Livraria-x86_64.AppImage (executável portável)"
fi
echo ""
echo "Para executar:"
echo "  cd dist && ./run-livraria.sh"
if [ -f "dist/Livraria-x86_64.AppImage" ]; then
    echo "  ou: ./dist/Livraria-x86_64.AppImage"
fi
echo ""
