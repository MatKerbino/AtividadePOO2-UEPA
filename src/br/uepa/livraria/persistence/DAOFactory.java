package br.uepa.livraria.persistence;

import br.uepa.livraria.model.dao.ClienteDAO;
import br.uepa.livraria.model.dao.LivroDAO;
import br.uepa.livraria.model.dao.EditoraDAO;
import br.uepa.livraria.model.dao.CompraDAO;
import br.uepa.livraria.persistence.impl.ClienteDAOJDBC;
import br.uepa.livraria.persistence.impl.LivroDAOJDBC;
import br.uepa.livraria.persistence.impl.EditoraDAOJDBC;
import br.uepa.livraria.persistence.impl.CompraDAOJDBC;

 
public class DAOFactory {
    
     
    public static ClienteDAO createClienteDAO() {
        return new ClienteDAOJDBC();
    }
    
     
    public static LivroDAO createLivroDAO() {
        return new LivroDAOJDBC();
    }
    
     
    public static EditoraDAO createEditoraDAO() {
        return new EditoraDAOJDBC();
    }
    
     
    public static CompraDAO createCompraDAO() {
        return new CompraDAOJDBC();
    }
}

