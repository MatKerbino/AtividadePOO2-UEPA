package br.uepa.livraria.persistence.impl;

import br.uepa.livraria.db.DB;
import br.uepa.livraria.db.DbException;
import br.uepa.livraria.model.dao.EditoraDAO;
import br.uepa.livraria.model.entities.Editora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 
public class EditoraDAOJDBC implements EditoraDAO {
    
    @Override
    public void insert(Editora editora) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "INSERT INTO Editora (nome, endereco, telefone, email) " +
                "VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            st.setString(1, editora.getNome());
            st.setString(2, editora.getEndereco());
            st.setString(3, editora.getTelefone());
            st.setString(4, editora.getEmail());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    editora.setId(id);
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
    
    @Override
    public void update(Editora editora) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement(
                "UPDATE Editora SET nome = ?, endereco = ?, telefone = ?, email = ? " +
                "WHERE id = ?"
            );
            
            st.setString(1, editora.getNome());
            st.setString(2, editora.getEndereco());
            st.setString(3, editora.getTelefone());
            st.setString(4, editora.getEmail());
            st.setInt(5, editora.getId());
            
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
            st = conn.prepareStatement("DELETE FROM Editora WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public Editora findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Editora WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return instantiateEditora(rs);
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
    public List<Editora> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Editora ORDER BY nome");
            rs = st.executeQuery();
            
            List<Editora> list = new ArrayList<>();
            while (rs.next()) {
                Editora editora = instantiateEditora(rs);
                list.add(editora);
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
    public List<Editora> findByName(String nome) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Editora WHERE nome LIKE ? ORDER BY nome");
            st.setString(1, "%" + nome + "%");
            rs = st.executeQuery();
            
            List<Editora> list = new ArrayList<>();
            while (rs.next()) {
                Editora editora = instantiateEditora(rs);
                list.add(editora);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
     
    private Editora instantiateEditora(ResultSet rs) throws SQLException {
        Editora editora = new Editora();
        editora.setId(rs.getInt("id"));
        editora.setNome(rs.getString("nome"));
        editora.setEndereco(rs.getString("endereco"));
        editora.setTelefone(rs.getString("telefone"));
        editora.setEmail(rs.getString("email"));
        return editora;
    }
}


