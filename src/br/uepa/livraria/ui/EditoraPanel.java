package br.uepa.livraria.ui;

import br.uepa.livraria.model.dao.EditoraDAO;
import br.uepa.livraria.model.entities.Editora;
import br.uepa.livraria.persistence.DAOFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

 
public class EditoraPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private EditoraDAO editoraDAO;
    private JTable editoraTable;
    private DefaultTableModel tableModel;
    private JTextField nomeField;
    private JTextField enderecoField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JButton btnInserir;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;
    private JButton btnBuscar;
    private JTextField buscarField;
    
    private Editora editoraSelecionada;
    
     
    public EditoraPanel() {
        editoraDAO = DAOFactory.createEditoraDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
     
    private void initializeComponents() {
         
        nomeField = new JTextField(25);
        enderecoField = new JTextField(30);
        telefoneField = new JTextField(15);
        emailField = new JTextField(25);
        
         
        btnInserir = new JButton("Inserir");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        btnBuscar = new JButton("Buscar");
        
         
        buscarField = new JTextField(20);
        
         
        String[] colunas = {"ID", "Nome", "Endereço", "Telefone", "Email"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  
            }
        };
        editoraTable = new JTable(tableModel);
        editoraTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        editoraTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarEditora();
            }
        });
    }
    
     
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
         
        JPanel formPanel = createFormPanel();
        
         
        JScrollPane scrollPane = new JScrollPane(editoraTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
         
        JPanel searchPanel = createSearchPanel();
        
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }
    
     
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados da Editora"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
         
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(nomeField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(telefoneField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(enderecoField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailField, gbc);
        
         
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnInserir);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
     
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Editora"));
        panel.add(new JLabel("Nome:"));
        panel.add(buscarField);
        panel.add(btnBuscar);
        return panel;
    }
    
     
    private void setupEventHandlers() {
        btnInserir.addActionListener(e -> inserirEditora());
        btnAtualizar.addActionListener(e -> atualizarEditora());
        btnExcluir.addActionListener(e -> excluirEditora());
        btnLimpar.addActionListener(e -> limparCampos());
        btnBuscar.addActionListener(e -> buscarEditoras());
    }
    
     
    private void inserirEditora() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            Editora editora = criarEditora();
            editoraDAO.insert(editora);
            
            JOptionPane.showMessageDialog(this, "Editora inserida com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir editora: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void atualizarEditora() {
        try {
            if (editoraSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma editora para atualizar!", 
                                            "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            Editora editora = criarEditora();
            editora.setId(editoraSelecionada.getId());
            editoraDAO.update(editora);
            
            JOptionPane.showMessageDialog(this, "Editora atualizada com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar editora: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void excluirEditora() {
        try {
            if (editoraSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma editora para excluir!", 
                                            "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir a editora selecionada?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                editoraDAO.deleteById(editoraSelecionada.getId());
                JOptionPane.showMessageDialog(this, "Editora excluída com sucesso!", 
                                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                refreshData();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir editora: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void limparCampos() {
        nomeField.setText("");
        enderecoField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        editoraSelecionada = null;
        editoraTable.clearSelection();
    }
    
     
    private void buscarEditoras() {
        String nome = buscarField.getText().trim();
        if (nome.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Editora> editoras = editoraDAO.findByName(nome);
                atualizarTabela(editoras);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar editoras: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
     
    private void selecionarEditora() {
        int selectedRow = editoraTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
                editoraSelecionada = editoraDAO.findById(id);
                preencherCampos(editoraSelecionada);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar editora: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
     
    private void preencherCampos(Editora editora) {
        nomeField.setText(editora.getNome());
        enderecoField.setText(editora.getEndereco());
        telefoneField.setText(editora.getTelefone());
        emailField.setText(editora.getEmail());
    }
    
     
    private Editora criarEditora() {
        String nome = nomeField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String email = emailField.getText().trim();
        
        return new Editora(null, nome, endereco, telefone, email);
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
        
        return true;
    }
    
     
    private void atualizarTabela(List<Editora> editoras) {
        tableModel.setRowCount(0);
        for (Editora editora : editoras) {
            Object[] row = new Object[5];
            row[0] = editora.getId();
            row[1] = editora.getNome();
            row[2] = editora.getEndereco();
            row[3] = editora.getTelefone();
            row[4] = editora.getEmail();
            
            tableModel.addRow(row);
        }
    }
    
     
    public void refreshData() {
        try {
            List<Editora> editoras = editoraDAO.findAll();
            atualizarTabela(editoras);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar editoras: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
