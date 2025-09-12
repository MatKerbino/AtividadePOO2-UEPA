package br.uepa.livraria.model.entities;

import java.math.BigDecimal;
import java.util.Objects;

 
public class Livro {
    
    private String isbn;
    private String tituloAutor;
    private String genero;
    private Integer anoPublicacao;
    private BigDecimal preco;
    private Integer estoque;
    private Integer idEditora;
    
     
    public Livro() {
    }
    
     
    public Livro(String isbn, String tituloAutor, String genero, Integer anoPublicacao, 
                 BigDecimal preco, Integer estoque, Integer idEditora) {
        this.isbn = isbn;
        this.tituloAutor = tituloAutor;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.preco = preco;
        this.estoque = estoque;
        this.idEditora = idEditora;
    }
    
     
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTituloAutor() {
        return tituloAutor;
    }
    
    public void setTituloAutor(String tituloAutor) {
        this.tituloAutor = tituloAutor;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }
    
    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
    public Integer getEstoque() {
        return estoque;
    }
    
    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
    
    public Integer getIdEditora() {
        return idEditora;
    }
    
    public void setIdEditora(Integer idEditora) {
        this.idEditora = idEditora;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(isbn, livro.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
    
    @Override
    public String toString() {
        return "Livro{" +
                "isbn='" + isbn + '\'' +
                ", tituloAutor='" + tituloAutor + '\'' +
                ", genero='" + genero + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", preco=" + preco +
                ", estoque=" + estoque +
                ", idEditora=" + idEditora +
                '}';
    }
}

