package br.uepa.livraria.model.entities;

 
public class PessoaJuridica extends Cliente {
    
    private String cnpj;
    private String razaoSocial;
    
     
    public PessoaJuridica() {
        super();
    }
    
     
    public PessoaJuridica(Integer id, String nome, String endereco, String telefone, String email, 
                          String cnpj, String razaoSocial) {
        super(id, nome, endereco, telefone, email);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }
    
     
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getRazaoSocial() {
        return razaoSocial;
    }
    
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    
    @Override
    public String getTipoCliente() {
        return "Pessoa Jurídica";
    }
    
    @Override
    public String toString() {
        return "PessoaJuridica{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                '}';
    }
}

