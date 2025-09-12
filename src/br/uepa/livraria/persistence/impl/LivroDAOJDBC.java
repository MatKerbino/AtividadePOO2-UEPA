package br.uepa.livraria.persistence.impl;

import br.uepa.livraria.db.DB;
import br.uepa.livraria.db.DbException;
import br.uepa.livraria.model.dao.LivroDAO;
import br.uepa.livraria.model.entities.Livro;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 
public class LivroDAOJDBC implements LivroDAO {
    
    @Override
    public void insert(Livro livro) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "INSERT INTO Livro (isbn, titulo_autor, genero, ano_publicacao, preco, estoque, id_editora) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            
            st.setString(1, livro.getIsbn());
            st.setString(2, livro.getTituloAutor());
            st.setString(3, livro.getGenero());
            st.setInt(4, livro.getAnoPublicacao());
            st.setBigDecimal(5, livro.getPreco());
            st.setInt(6, livro.getEstoque());
            st.setInt(7, livro.getIdEditora());
            
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void update(Livro livro) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "UPDATE Livro SET titulo_autor = ?, genero = ?, ano_publicacao = ?, " +
                "preco = ?, estoque = ?, id_editora = ? WHERE isbn = ?"
            );
            
            st.setString(1, livro.getTituloAutor());
            st.setString(2, livro.getGenero());
            st.setInt(3, livro.getAnoPublicacao());
            st.setBigDecimal(4, livro.getPreco());
            st.setInt(5, livro.getEstoque());
            st.setInt(6, livro.getIdEditora());
            st.setString(7, livro.getIsbn());
            
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void deleteByIsbn(String isbn) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("DELETE FROM Livro WHERE isbn = ?");
            st.setString(1, isbn);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public Livro findByIsbn(String isbn) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Livro WHERE isbn = ?");
            st.setString(1, isbn);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return instantiateLivro(rs);
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
    public List<Livro> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Livro ORDER BY titulo_autor");
            rs = st.executeQuery();
            
            List<Livro> list = new ArrayList<>();
            while (rs.next()) {
                Livro livro = instantiateLivro(rs);
                list.add(livro);
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
    public List<Livro> findByTituloAutor(String tituloAutor) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Livro WHERE titulo_autor LIKE ? ORDER BY titulo_autor");
            st.setString(1, "%" + tituloAutor + "%");
            rs = st.executeQuery();
            
            List<Livro> list = new ArrayList<>();
            while (rs.next()) {
                Livro livro = instantiateLivro(rs);
                list.add(livro);
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
    public List<Livro> findByGenero(String genero) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Livro WHERE genero = ? ORDER BY titulo_autor");
            st.setString(1, genero);
            rs = st.executeQuery();
            
            List<Livro> list = new ArrayList<>();
            while (rs.next()) {
                Livro livro = instantiateLivro(rs);
                list.add(livro);
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
    public void updateEstoque(String isbn, Integer novaQuantidade) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("UPDATE Livro SET estoque = ? WHERE isbn = ?");
            st.setInt(1, novaQuantidade);
            st.setString(2, isbn);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void decrementarEstoque(String isbn, Integer quantidade) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("UPDATE Livro SET estoque = estoque - ? WHERE isbn = ?");
            st.setInt(1, quantidade);
            st.setString(2, isbn);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
     
    private Livro instantiateLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setIsbn(rs.getString("isbn"));
        livro.setTituloAutor(rs.getString("titulo_autor"));
        livro.setGenero(rs.getString("genero"));
        livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
        livro.setPreco(rs.getBigDecimal("preco"));
        livro.setEstoque(rs.getInt("estoque"));
        livro.setIdEditora(rs.getInt("id_editora"));
        return livro;
    }
}


