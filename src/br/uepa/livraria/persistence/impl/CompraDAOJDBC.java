package br.uepa.livraria.persistence.impl;

import br.uepa.livraria.db.DB;
import br.uepa.livraria.db.DbException;
import br.uepa.livraria.model.dao.CompraDAO;
import br.uepa.livraria.model.entities.Compra;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

 
public class CompraDAOJDBC implements CompraDAO {
    
    @Override
    public void insert(Compra compra) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "INSERT INTO Compra (isbn_livro, id_cliente, quantidade, preco_unitario, data_compra) " +
                "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            st.setString(1, compra.getIsbnLivro());
            st.setInt(2, compra.getIdCliente());
            st.setInt(3, compra.getQuantidade());
            st.setBigDecimal(4, compra.getPrecoUnitario());
            st.setTimestamp(5, Timestamp.valueOf(compra.getDataCompra()));
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    compra.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha foi inserida.");
            }
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
     
    public void insertComTransacao(Compra compra, Integer quantidade) {
        Connection conn = null;
        PreparedStatement stCompra = null;
        PreparedStatement stEstoque = null;
        
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);  
            
             
            stCompra = conn.prepareStatement(
                "INSERT INTO Compra (isbn_livro, id_cliente, quantidade, preco_unitario, data_compra) " +
                "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            stCompra.setString(1, compra.getIsbnLivro());
            stCompra.setInt(2, compra.getIdCliente());
            stCompra.setInt(3, compra.getQuantidade());
            stCompra.setBigDecimal(4, compra.getPrecoUnitario());
            stCompra.setTimestamp(5, Timestamp.valueOf(compra.getDataCompra()));
            
            stCompra.executeUpdate();
            
             
            stEstoque = conn.prepareStatement("UPDATE Livro SET estoque = estoque - ? WHERE isbn = ?");
            stEstoque.setInt(1, quantidade);
            stEstoque.setString(2, compra.getIsbnLivro());
            
            int rowsAffected = stEstoque.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Livro não encontrado ou estoque insuficiente");
            }
            
            conn.commit();  
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  
                }
            } catch (SQLException ex) {
                throw new DbException("Erro ao reverter transação: " + ex.getMessage());
            }
            throw new DbException("Erro na transação: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);  
                }
            } catch (SQLException e) {
                throw new DbException("Erro ao restaurar auto-commit: " + e.getMessage());
            }
            DB.closeStatement(stCompra);
            DB.closeStatement(stEstoque);
        }
    }
    
    @Override
    public void update(Compra compra) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "UPDATE Compra SET isbn_livro = ?, id_cliente = ?, quantidade = ?, " +
                "preco_unitario = ?, data_compra = ? WHERE id = ?"
            );
            
            st.setString(1, compra.getIsbnLivro());
            st.setInt(2, compra.getIdCliente());
            st.setInt(3, compra.getQuantidade());
            st.setBigDecimal(4, compra.getPrecoUnitario());
            st.setTimestamp(5, Timestamp.valueOf(compra.getDataCompra()));
            st.setInt(6, compra.getId());
            
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("DELETE FROM Compra WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public Compra findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Compra WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return instantiateCompra(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    @Override
    public List<Compra> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Compra ORDER BY data_compra DESC");
            rs = st.executeQuery();
            
            List<Compra> list = new ArrayList<>();
            while (rs.next()) {
                Compra compra = instantiateCompra(rs);
                list.add(compra);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    @Override
    public List<Compra> findByCliente(Integer idCliente) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Compra WHERE id_cliente = ? ORDER BY data_compra DESC");
            st.setInt(1, idCliente);
            rs = st.executeQuery();
            
            List<Compra> list = new ArrayList<>();
            while (rs.next()) {
                Compra compra = instantiateCompra(rs);
                list.add(compra);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    @Override
    public List<Compra> findByLivro(String isbnLivro) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Compra WHERE isbn_livro = ? ORDER BY data_compra DESC");
            st.setString(1, isbnLivro);
            rs = st.executeQuery();
            
            List<Compra> list = new ArrayList<>();
            while (rs.next()) {
                Compra compra = instantiateCompra(rs);
                list.add(compra);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    @Override
    public List<Compra> findByPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Compra WHERE data_compra BETWEEN ? AND ? ORDER BY data_compra DESC");
            st.setTimestamp(1, Timestamp.valueOf(dataInicio));
            st.setTimestamp(2, Timestamp.valueOf(dataFim));
            rs = st.executeQuery();
            
            List<Compra> list = new ArrayList<>();
            while (rs.next()) {
                Compra compra = instantiateCompra(rs);
                list.add(compra);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    @Override
    public List<Object[]> findTop5LivrosMaisVendidos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "SELECT l.titulo_autor, SUM(c.quantidade) as total_vendas " +
                "FROM Compra c " +
                "JOIN Livro l ON c.isbn_livro = l.isbn " +
                "GROUP BY l.titulo_autor " +
                "ORDER BY total_vendas DESC " +
                "LIMIT 5"
            );
            rs = st.executeQuery();
            
            List<Object[]> list = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("titulo_autor");
                row[1] = rs.getInt("total_vendas");
                list.add(row);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
     
    private Compra instantiateCompra(ResultSet rs) throws SQLException {
        Compra compra = new Compra();
        compra.setId(rs.getInt("id"));
        compra.setIsbnLivro(rs.getString("isbn_livro"));
        compra.setIdCliente(rs.getInt("id_cliente"));
        compra.setQuantidade(rs.getInt("quantidade"));
        compra.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
        compra.setDataCompra(rs.getTimestamp("data_compra").toLocalDateTime());
        return compra;
    }
}


