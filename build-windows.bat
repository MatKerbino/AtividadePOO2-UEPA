@echo off
echo === Compilando Sistema de Gerenciamento de Livraria para Windows ===

REM Verifica se o driver JDBC existe
if not exist "mysql-connector-j-9.3.0.jar" (
    echo ERRO: Driver JDBC do MySQL nao encontrado!
    echo Baixe o mysql-connector-j-9.3.0.jar e coloque no diretorio do projeto.
    pause
    exit /b 1
)

REM Cria diretorio de classes se nao existir
if not exist "classes" mkdir classes

REM Compila todos os arquivos Java
echo Compilando arquivos Java...
javac -cp ".;mysql-connector-j-9.3.0.jar" -d classes src\br\uepa\livraria\**\*.java

if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo Compilacao concluida com sucesso!

REM Cria diretorio de distribuicao
if not exist "dist" mkdir dist

REM Copia arquivos necessarios
copy mysql-connector-j-9.3.0.jar dist\
copy db.properties dist\
copy schema.sql dist\

REM Cria o arquivo JAR executavel
echo Criando arquivo JAR executavel...
cd classes
jar cfe ..\dist\livraria.jar br.uepa.livraria.application.Main br\uepa\livraria\
cd ..

REM Cria o arquivo batch para executar
echo @echo off > dist\run-livraria.bat
echo java -cp "livraria.jar;mysql-connector-j-9.3.0.jar" br.uepa.livraria.application.Main >> dist\run-livraria.bat
echo pause >> dist\run-livraria.bat

echo.
echo === Build concluido com sucesso! ===
echo.
echo Arquivos gerados em: dist\
echo - livraria.jar (executavel principal)
echo - run-livraria.bat (script para executar)
echo - mysql-connector-j-9.3.0.jar (driver do banco)
echo - db.properties (configuracao do banco)
echo - schema.sql (script do banco)
echo.
echo Para executar: cd dist && run-livraria.bat
echo.
pause
