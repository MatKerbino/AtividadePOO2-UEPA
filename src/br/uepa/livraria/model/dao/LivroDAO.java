package br.uepa.livraria.model.dao;

import br.uepa.livraria.model.entities.Livro;
import java.util.List;

 
public interface LivroDAO {
    
     
    void insert(Livro livro);
    
     
    void update(Livro livro);
    
     
    void deleteByIsbn(String isbn);
    
     
    Livro findByIsbn(String isbn);
    
     
    List<Livro> findAll();
    
     
    List<Livro> findByTituloAutor(String tituloAutor);
    
     
    List<Livro> findByGenero(String genero);
    
     
    void updateEstoque(String isbn, Integer novaQuantidade);
    
     
    void decrementarEstoque(String isbn, Integer quantidade);
}

