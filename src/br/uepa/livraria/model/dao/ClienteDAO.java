package br.uepa.livraria.model.dao;

import br.uepa.livraria.model.entities.Cliente;
import java.util.List;

 
public interface ClienteDAO {
    
     
    void insert(Cliente cliente);
    
     
    void update(Cliente cliente);
    
     
    void deleteById(Integer id);
    
     
    Cliente findById(Integer id);
    
     
    List<Cliente> findAll();
    
     
    List<Cliente> findByName(String nome);
}

