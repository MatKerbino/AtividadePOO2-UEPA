package br.uepa.livraria.model.dao;

import br.uepa.livraria.model.entities.Compra;
import java.time.LocalDateTime;
import java.util.List;

 
public interface CompraDAO {
    
     
    void insert(Compra compra);
    
     
    void update(Compra compra);
    
     
    void deleteById(Integer id);
    
     
    Compra findById(Integer id);
    
     
    List<Compra> findAll();
    
     
    List<Compra> findByCliente(Integer idCliente);
    
     
    List<Compra> findByLivro(String isbnLivro);
    
     
    List<Compra> findByPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);
    
     
    List<Object[]> findTop5LivrosMaisVendidos();
}

