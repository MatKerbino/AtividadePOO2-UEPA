#!/bin/bash
cd "$(dirname "$0")"
java -cp "livraria.jar:mysql-connector-j-9.3.0.jar" br.uepa.livraria.application.Main
