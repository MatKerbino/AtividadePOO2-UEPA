package br.uepa.livraria.persistence.impl;

import br.uepa.livraria.db.DB;
import br.uepa.livraria.db.DbException;
import br.uepa.livraria.model.dao.ClienteDAO;
import br.uepa.livraria.model.entities.Cliente;
import br.uepa.livraria.model.entities.PessoaFisica;
import br.uepa.livraria.model.entities.PessoaJuridica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 
public class ClienteDAOJDBC implements ClienteDAO {
    
    @Override
    public void insert(Cliente cliente) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            
            if (cliente instanceof PessoaFisica) {
                st = conn.prepareStatement(
                    "INSERT INTO Cliente (nome, endereco, telefone, email, tipo_cliente, cpf) " +
                    "VALUES (?, ?, ?, ?, 'PF', ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                st.setString(5, ((PessoaFisica) cliente).getCpf());
            } else if (cliente instanceof PessoaJuridica) {
                st = conn.prepareStatement(
                    "INSERT INTO Cliente (nome, endereco, telefone, email, tipo_cliente, cnpj, razao_social) " +
                    "VALUES (?, ?, ?, ?, 'PJ', ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                st.setString(5, ((PessoaJuridica) cliente).getCnpj());
                st.setString(6, ((PessoaJuridica) cliente).getRazaoSocial());
            }
            
            st.setString(1, cliente.getNome());
            st.setString(2, cliente.getEndereco());
            st.setString(3, cliente.getTelefone());
            st.setString(4, cliente.getEmail());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    cliente.setId(id);
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
    public void update(Cliente cliente) {
        PreparedStatement st = null;
        try {
            Connection conn = DB.getConnection();
            
            if (cliente instanceof PessoaFisica) {
                st = conn.prepareStatement(
                    "UPDATE Cliente SET nome = ?, endereco = ?, telefone = ?, email = ?, cpf = ? " +
                    "WHERE id = ?"
                );
                st.setString(5, ((PessoaFisica) cliente).getCpf());
            } else if (cliente instanceof PessoaJuridica) {
                st = conn.prepareStatement(
                    "UPDATE Cliente SET nome = ?, endereco = ?, telefone = ?, email = ?, cnpj = ?, razao_social = ? " +
                    "WHERE id = ?"
                );
                st.setString(5, ((PessoaJuridica) cliente).getCnpj());
                st.setString(6, ((PessoaJuridica) cliente).getRazaoSocial());
                st.setInt(7, cliente.getId());
            }
            
            st.setString(1, cliente.getNome());
            st.setString(2, cliente.getEndereco());
            st.setString(3, cliente.getTelefone());
            st.setString(4, cliente.getEmail());
            
            if (cliente instanceof PessoaFisica) {
                st.setInt(6, cliente.getId());
            }
            
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
            st = conn.prepareStatement("DELETE FROM Cliente WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public Cliente findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Cliente WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return instantiateCliente(rs);
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
    public List<Cliente> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Cliente ORDER BY nome");
            rs = st.executeQuery();
            
            List<Cliente> list = new ArrayList<>();
            while (rs.next()) {
                Cliente cliente = instantiateCliente(rs);
                list.add(cliente);
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
    public List<Cliente> findByName(String nome) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DB.getConnection();
            st = conn.prepareStatement("SELECT * FROM Cliente WHERE nome LIKE ? ORDER BY nome");
            st.setString(1, "%" + nome + "%");
            rs = st.executeQuery();
            
            List<Cliente> list = new ArrayList<>();
            while (rs.next()) {
                Cliente cliente = instantiateCliente(rs);
                list.add(cliente);
            }
            return list;
            
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
     
    private Cliente instantiateCliente(ResultSet rs) throws SQLException {
        String tipoCliente = rs.getString("tipo_cliente");
        
        if ("PF".equals(tipoCliente)) {
            PessoaFisica pf = new PessoaFisica();
            pf.setId(rs.getInt("id"));
            pf.setNome(rs.getString("nome"));
            pf.setEndereco(rs.getString("endereco"));
            pf.setTelefone(rs.getString("telefone"));
            pf.setEmail(rs.getString("email"));
            pf.setCpf(rs.getString("cpf"));
            return pf;
        } else if ("PJ".equals(tipoCliente)) {
            PessoaJuridica pj = new PessoaJuridica();
            pj.setId(rs.getInt("id"));
            pj.setNome(rs.getString("nome"));
            pj.setEndereco(rs.getString("endereco"));
            pj.setTelefone(rs.getString("telefone"));
            pj.setEmail(rs.getString("email"));
            pj.setCnpj(rs.getString("cnpj"));
            pj.setRazaoSocial(rs.getString("razao_social"));
            return pj;
        }
        
        return null;
    }
}

