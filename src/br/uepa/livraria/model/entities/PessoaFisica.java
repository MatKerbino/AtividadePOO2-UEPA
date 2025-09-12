package br.uepa.livraria.model.entities;

 
public class PessoaFisica extends Cliente {
    
    private String cpf;
    
     
    public PessoaFisica() {
        super();
    }
    
     
    public PessoaFisica(Integer id, String nome, String endereco, String telefone, String email, String cpf) {
        super(id, nome, endereco, telefone, email);
        this.cpf = cpf;
    }
    
     
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    @Override
    public String getTipoCliente() {
        return "Pessoa Física";
    }
    
    @Override
    public String toString() {
        return "PessoaFisica{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}

