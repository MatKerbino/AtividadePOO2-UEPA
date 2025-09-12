package br.uepa.livraria.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

 
public class Compra {
    
    private Integer id;
    private String isbnLivro;
    private Integer idCliente;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private LocalDateTime dataCompra;
    
     
    public Compra() {
    }
    
     
    public Compra(Integer id, String isbnLivro, Integer idCliente, Integer quantidade, 
                  BigDecimal precoUnitario, LocalDateTime dataCompra) {
        this.id = id;
        this.isbnLivro = isbnLivro;
        this.idCliente = idCliente;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.dataCompra = dataCompra;
    }
    
     
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getIsbnLivro() {
        return isbnLivro;
    }
    
    public void setIsbnLivro(String isbnLivro) {
        this.isbnLivro = isbnLivro;
    }
    
    public Integer getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
    
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    public LocalDateTime getDataCompra() {
        return dataCompra;
    }
    
    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }
    
     
    public BigDecimal getValorTotal() {
        if (quantidade != null && precoUnitario != null) {
            return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Objects.equals(id, compra.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", isbnLivro='" + isbnLivro + '\'' +
                ", idCliente=" + idCliente +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", dataCompra=" + dataCompra +
                '}';
    }
}

