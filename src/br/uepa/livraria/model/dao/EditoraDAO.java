package br.uepa.livraria.model.dao;

import br.uepa.livraria.model.entities.Editora;
import java.util.List;

 
public interface EditoraDAO {
    
     
    void insert(Editora editora);
    
     
    void update(Editora editora);
    
     
    void deleteById(Integer id);
    
     
    Editora findById(Integer id);
    
     
    List<Editora> findAll();
    
     
    List<Editora> findByName(String nome);
}

