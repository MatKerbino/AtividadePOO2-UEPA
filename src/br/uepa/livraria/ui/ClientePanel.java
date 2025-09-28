package br.uepa.livraria.ui;

import br.uepa.livraria.model.dao.ClienteDAO;
import br.uepa.livraria.model.entities.Cliente;
import br.uepa.livraria.model.entities.PessoaFisica;
import br.uepa.livraria.model.entities.PessoaJuridica;
import br.uepa.livraria.persistence.DAOFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

 
public class ClientePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private ClienteDAO clienteDAO;
    private JTable clienteTable;
    private DefaultTableModel tableModel;
    private JTextField nomeField;
    private JTextField enderecoField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JTextField cpfField;
    private JTextField cnpjField;
    private JTextField razaoSocialField;
    private JComboBox<String> tipoClienteCombo;
    private JButton btnInserir;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;
    private JButton btnBuscar;
    private JTextField buscarField;
    
    private Cliente clienteSelecionado;
    
     
    public ClientePanel() {
        clienteDAO = DAOFactory.createClienteDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
     
    private void initializeComponents() {
         
        nomeField = new JTextField(25);
        nomeField.setPreferredSize(new Dimension(300, 30));
        enderecoField = new JTextField(25);
        enderecoField.setPreferredSize(new Dimension(300, 30));
        telefoneField = new JTextField(20);
        telefoneField.setPreferredSize(new Dimension(200, 30));
        emailField = new JTextField(25);
        emailField.setPreferredSize(new Dimension(300, 30));
        cpfField = new JTextField(20);
        cpfField.setPreferredSize(new Dimension(200, 30));
        cnpjField = new JTextField(20);
        cnpjField.setPreferredSize(new Dimension(200, 30));
        razaoSocialField = new JTextField(25);
        razaoSocialField.setPreferredSize(new Dimension(300, 30));
        
         
        tipoClienteCombo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
        tipoClienteCombo.setPreferredSize(new Dimension(200, 30));
        tipoClienteCombo.addActionListener(e -> toggleCamposEspecificos());
        
         
        btnInserir = new JButton("Inserir");
        btnInserir.setPreferredSize(new Dimension(100, 35));
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(100, 35));
        btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(100, 35));
        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 35));
        
         
        buscarField = new JTextField(20);
        buscarField.setPreferredSize(new Dimension(250, 30));
        
         
        String[] colunas = {"ID", "Nome", "Tipo", "CPF/CNPJ", "Telefone", "Email"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  
            }
        };
        clienteTable = new JTable(tableModel);
        clienteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clienteTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarCliente();
            }
        });
        
         
        toggleCamposEspecificos();
    }
    
     
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
         
        JPanel formPanel = createFormPanel();
        
         
        JScrollPane scrollPane = new JScrollPane(clienteTable);
        scrollPane.setPreferredSize(new Dimension(1000, 350));
        
         
        JPanel searchPanel = createSearchPanel();
        
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }
    
     
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
         
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(tipoClienteCombo, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(nomeField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(enderecoField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(telefoneField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailField, gbc);
        
         
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(cpfField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(cnpjField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Razão Social:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(razaoSocialField, gbc);
        
         
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnInserir);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
     
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Cliente"));
        panel.add(new JLabel("Nome:"));
        panel.add(buscarField);
        panel.add(btnBuscar);
        return panel;
    }
    
     
    private void setupEventHandlers() {
        btnInserir.addActionListener(e -> inserirCliente());
        btnAtualizar.addActionListener(e -> atualizarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        btnLimpar.addActionListener(e -> limparCampos());
        btnBuscar.addActionListener(e -> buscarClientes());
    }
    
     
    private void toggleCamposEspecificos() {
        boolean isPessoaFisica = tipoClienteCombo.getSelectedItem().equals("Pessoa Física");
        
        // Esconder/mostrar campos específicos
        cpfField.setVisible(isPessoaFisica);
        cnpjField.setVisible(!isPessoaFisica);
        razaoSocialField.setVisible(!isPessoaFisica);
        
        // Esconder/mostrar labels correspondentes
        if (cpfField.getParent() != null) {
            Component[] components = ((JPanel) cpfField.getParent()).getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getText().equals("CPF:")) {
                        label.setVisible(isPessoaFisica);
                    } else if (label.getText().equals("CNPJ:")) {
                        label.setVisible(!isPessoaFisica);
                    } else if (label.getText().equals("Razão Social:")) {
                        label.setVisible(!isPessoaFisica);
                    }
                }
            }
        }
        
        revalidate();
        repaint();
    }
    
     
    private void inserirCliente() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            Cliente cliente = criarCliente();
            clienteDAO.insert(cliente);
            
            JOptionPane.showMessageDialog(this, "Cliente inserido com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir cliente: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void atualizarCliente() {
        try {
            if (clienteSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para atualizar!", 
                                            "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            Cliente cliente = criarCliente();
            cliente.setId(clienteSelecionado.getId());
            clienteDAO.update(cliente);
            
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void excluirCliente() {
        try {
            if (clienteSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!", 
                                            "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir o cliente selecionado?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                clienteDAO.deleteById(clienteSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!", 
                                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                refreshData();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void limparCampos() {
        nomeField.setText("");
        enderecoField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        cpfField.setText("");
        cnpjField.setText("");
        razaoSocialField.setText("");
        tipoClienteCombo.setSelectedIndex(0);
        clienteSelecionado = null;
        clienteTable.clearSelection();
    }
    
     
    private void buscarClientes() {
        String nome = buscarField.getText().trim();
        if (nome.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Cliente> clientes = clienteDAO.findByName(nome);
                atualizarTabela(clientes);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar clientes: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
     
    private void selecionarCliente() {
        int selectedRow = clienteTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
                clienteSelecionado = clienteDAO.findById(id);
                preencherCampos(clienteSelecionado);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cliente: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
     
    private void preencherCampos(Cliente cliente) {
        nomeField.setText(cliente.getNome());
        enderecoField.setText(cliente.getEndereco());
        telefoneField.setText(cliente.getTelefone());
        emailField.setText(cliente.getEmail());
        
        if (cliente instanceof PessoaFisica) {
            tipoClienteCombo.setSelectedItem("Pessoa Física");
            cpfField.setText(((PessoaFisica) cliente).getCpf());
        } else if (cliente instanceof PessoaJuridica) {
            tipoClienteCombo.setSelectedItem("Pessoa Jurídica");
            cnpjField.setText(((PessoaJuridica) cliente).getCnpj());
            razaoSocialField.setText(((PessoaJuridica) cliente).getRazaoSocial());
        }
        
        toggleCamposEspecificos();
    }
    
     
    private Cliente criarCliente() {
        String nome = nomeField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String email = emailField.getText().trim();
        
        if (tipoClienteCombo.getSelectedItem().equals("Pessoa Física")) {
            String cpf = cpfField.getText().trim();
            return new PessoaFisica(null, nome, endereco, telefone, email, cpf);
        } else {
            String cnpj = cnpjField.getText().trim();
            String razaoSocial = razaoSocialField.getText().trim();
            return new PessoaJuridica(null, nome, endereco, telefone, email, cnpj, razaoSocial);
        }
    }
    
     
    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            nomeField.requestFocus();
            return false;
        }
        
        if (enderecoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Endereço é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            enderecoField.requestFocus();
            return false;
        }
        
        if (telefoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Telefone é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            telefoneField.requestFocus();
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        if (tipoClienteCombo.getSelectedItem().equals("Pessoa Física")) {
            if (cpfField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "CPF é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
                cpfField.requestFocus();
                return false;
            }
        } else {
            if (cnpjField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "CNPJ é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
                cnpjField.requestFocus();
                return false;
            }
            if (razaoSocialField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Razão Social é obrigatória!", "Validação", JOptionPane.WARNING_MESSAGE);
                razaoSocialField.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
     
    private void atualizarTabela(List<Cliente> clientes) {
        tableModel.setRowCount(0);
        for (Cliente cliente : clientes) {
            Object[] row = new Object[6];
            row[0] = cliente.getId();
            row[1] = cliente.getNome();
            row[2] = cliente.getTipoCliente();
            
            if (cliente instanceof PessoaFisica) {
                row[3] = ((PessoaFisica) cliente).getCpf();
            } else if (cliente instanceof PessoaJuridica) {
                row[3] = ((PessoaJuridica) cliente).getCnpj();
            }
            
            row[4] = cliente.getTelefone();
            row[5] = cliente.getEmail();
            
            tableModel.addRow(row);
        }
        
        // Aplicar cores diferentes para cada tipo de cliente
        clienteTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String tipoCliente = (String) table.getValueAt(row, 2);
                    if ("Pessoa Física".equals(tipoCliente)) {
                        c.setBackground(new Color(173, 216, 230)); // Azul claro
                    } else if ("Pessoa Jurídica".equals(tipoCliente)) {
                        c.setBackground(new Color(144, 238, 144)); // Verde claro
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                
                return c;
            }
        });
    }
    
     
    public void refreshData() {
        try {
            List<Cliente> clientes = clienteDAO.findAll();
            atualizarTabela(clientes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}


