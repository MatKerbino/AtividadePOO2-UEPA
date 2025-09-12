package br.uepa.livraria.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

 
public class DB {
    
    private static Connection conn = null;
    private static Properties props = loadProperties();
    
     
    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException("Erro ao carregar arquivo de propriedades: " + e.getMessage());
        }
    }
    
     
    public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = props.getProperty("dburl");
                String user = props.getProperty("user");
                String password = props.getProperty("password");
                
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new DbException("Erro ao conectar com o banco de dados: " + e.getMessage());
            }
        }
        return conn;
    }
    
     
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
     
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar Statement: " + e.getMessage());
            }
        }
    }
    
     
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar ResultSet: " + e.getMessage());
            }
        }
    }
}
