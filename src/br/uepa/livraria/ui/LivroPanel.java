package br.uepa.livraria.ui;

import br.uepa.livraria.model.dao.LivroDAO;
import br.uepa.livraria.model.dao.EditoraDAO;
import br.uepa.livraria.model.entities.Livro;
import br.uepa.livraria.model.entities.Editora;
import br.uepa.livraria.persistence.DAOFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

 
public class LivroPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private LivroDAO livroDAO;
    private EditoraDAO editoraDAO;
    private JTable livroTable;
    private DefaultTableModel tableModel;
    private JTextField isbnField;
    private JTextField tituloAutorField;
    private JTextField generoField;
    private JTextField anoPublicacaoField;
    private JTextField precoField;
    private JTextField estoqueField;
    private JComboBox<Editora> editoraCombo;
    private JButton btnInserir;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;
    private JButton btnBuscar;
    private JTextField buscarField;
    private JComboBox<String> generoCombo;
    
    private Livro livroSelecionado;
    
     
    public LivroPanel() {
        livroDAO = DAOFactory.createLivroDAO();
        editoraDAO = DAOFactory.createEditoraDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
     
    private void initializeComponents() {
         
        isbnField = new JTextField(20);
        isbnField.setPreferredSize(new Dimension(250, 35));
        tituloAutorField = new JTextField(30);
        tituloAutorField.setPreferredSize(new Dimension(400, 35));
        generoField = new JTextField(20);
        generoField.setPreferredSize(new Dimension(200, 35));
        anoPublicacaoField = new JTextField(15);
        anoPublicacaoField.setPreferredSize(new Dimension(150, 35));
        precoField = new JTextField(15);
        precoField.setPreferredSize(new Dimension(150, 35));
        estoqueField = new JTextField(15);
        estoqueField.setPreferredSize(new Dimension(150, 35));
        
         
        editoraCombo = new JComboBox<>();
        editoraCombo.setPreferredSize(new Dimension(300, 35));
        
         
        String[] generos = {"Ficção", "Romance", "Mistério", "Fantasia", "Ficção Científica", 
                           "Biografia", "História", "Autoajuda", "Técnico", "Infantil", "Outros"};
        generoCombo = new JComboBox<>(generos);
        generoCombo.setEditable(true);
        generoCombo.setPreferredSize(new Dimension(200, 35));
        
         
        btnInserir = new JButton("Inserir");
        btnInserir.setPreferredSize(new Dimension(100, 40));
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 40));
        btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(100, 40));
        btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(100, 40));
        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 40));
        
         
        buscarField = new JTextField(20);
        buscarField.setPreferredSize(new Dimension(250, 35));
        
         
        String[] colunas = {"ISBN", "Título/Autor", "Gênero", "Ano", "Preço", "Estoque", "Editora"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  
            }
        };
        livroTable = new JTable(tableModel);
        livroTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        livroTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLivro();
            }
        });
        
         
        carregarEditoras();
    }
    
     
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
         
        JPanel formPanel = createFormPanel();
        
         
        JScrollPane scrollPane = new JScrollPane(livroTable);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        
         
        JPanel searchPanel = createSearchPanel();
        
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }
    
     
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Livro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
         
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(isbnField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Título/Autor:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(tituloAutorField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(generoCombo, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(anoPublicacaoField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(precoField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(estoqueField, gbc);
        
         
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Editora:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(editoraCombo, gbc);
        
         
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
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Livro"));
        panel.add(new JLabel("Título/Autor:"));
        panel.add(buscarField);
        panel.add(btnBuscar);
        return panel;
    }
    
     
    private void setupEventHandlers() {
        btnInserir.addActionListener(e -> inserirLivro());
        btnAtualizar.addActionListener(e -> atualizarLivro());
        btnExcluir.addActionListener(e -> excluirLivro());
        btnLimpar.addActionListener(e -> limparCampos());
        btnBuscar.addActionListener(e -> buscarLivros());
    }
    
     
    private void carregarEditoras() {
        try {
            List<Editora> editoras = editoraDAO.findAll();
            editoraCombo.removeAllItems();
            for (Editora editora : editoras) {
                editoraCombo.addItem(editora);
            }
            
            // Configurar renderer customizado para mostrar apenas o nome da editora
            editoraCombo.setRenderer(new javax.swing.ListCellRenderer<Editora>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Editora> list, Editora value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = new JLabel();
                    if (value != null) {
                        label.setText(value.getNome());
                    }
                    if (isSelected) {
                        label.setBackground(list.getSelectionBackground());
                        label.setForeground(list.getSelectionForeground());
                    } else {
                        label.setBackground(list.getBackground());
                        label.setForeground(list.getForeground());
                    }
                    label.setOpaque(true);
                    return label;
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar editoras: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void inserirLivro() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            Livro livro = criarLivro();
            livroDAO.insert(livro);
            
            JOptionPane.showMessageDialog(this, "Livro inserido com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir livro: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void atualizarLivro() {
        try {
            if (livroSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um livro para atualizar!", 
                                            "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            Livro livro = criarLivro();
            livro.setIsbn(livroSelecionado.getIsbn());
            livroDAO.update(livro);
            
            JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar livro: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void excluirLivro() {
        try {
            if (livroSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um livro para excluir!", 
                                            "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir o livro selecionado?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                livroDAO.deleteByIsbn(livroSelecionado.getIsbn());
                JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!", 
                                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                refreshData();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir livro: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void limparCampos() {
        isbnField.setText("");
        tituloAutorField.setText("");
        generoCombo.setSelectedIndex(0);
        anoPublicacaoField.setText("");
        precoField.setText("");
        estoqueField.setText("");
        editoraCombo.setSelectedIndex(0);
        livroSelecionado = null;
        livroTable.clearSelection();
    }
    
     
    private void buscarLivros() {
        String tituloAutor = buscarField.getText().trim();
        if (tituloAutor.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Livro> livros = livroDAO.findByTituloAutor(tituloAutor);
                atualizarTabela(livros);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar livros: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
     
    private void selecionarLivro() {
        int selectedRow = livroTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String isbn = (String) tableModel.getValueAt(selectedRow, 0);
                livroSelecionado = livroDAO.findByIsbn(isbn);
                preencherCampos(livroSelecionado);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar livro: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
     
    private void preencherCampos(Livro livro) {
        isbnField.setText(livro.getIsbn());
        tituloAutorField.setText(livro.getTituloAutor());
        generoCombo.setSelectedItem(livro.getGenero());
        anoPublicacaoField.setText(livro.getAnoPublicacao().toString());
        precoField.setText(livro.getPreco().toString());
        estoqueField.setText(livro.getEstoque().toString());
        
         
        for (int i = 0; i < editoraCombo.getItemCount(); i++) {
            Editora editora = editoraCombo.getItemAt(i);
            if (editora.getId().equals(livro.getIdEditora())) {
                editoraCombo.setSelectedIndex(i);
                break;
            }
        }
    }
    
     
    private Livro criarLivro() {
        String isbn = isbnField.getText().trim();
        String tituloAutor = tituloAutorField.getText().trim();
        String genero = (String) generoCombo.getSelectedItem();
        Integer anoPublicacao = Integer.parseInt(anoPublicacaoField.getText().trim());
        BigDecimal preco = new BigDecimal(precoField.getText().trim());
        Integer estoque = Integer.parseInt(estoqueField.getText().trim());
        Editora editora = (Editora) editoraCombo.getSelectedItem();
        
        return new Livro(isbn, tituloAutor, genero, anoPublicacao, preco, estoque, editora.getId());
    }
    
     
    private boolean validarCampos() {
        if (isbnField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ISBN é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            isbnField.requestFocus();
            return false;
        }
        
        if (tituloAutorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título/Autor é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            tituloAutorField.requestFocus();
            return false;
        }
        
        if (anoPublicacaoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ano de publicação é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            anoPublicacaoField.requestFocus();
            return false;
        }
        
        if (precoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preço é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            precoField.requestFocus();
            return false;
        }
        
        if (estoqueField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Estoque é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            estoqueField.requestFocus();
            return false;
        }
        
        if (editoraCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Editora é obrigatória!", "Validação", JOptionPane.WARNING_MESSAGE);
            editoraCombo.requestFocus();
            return false;
        }
        
         
        try {
            Integer.parseInt(anoPublicacaoField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano de publicação deve ser um número válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            anoPublicacaoField.requestFocus();
            return false;
        }
        
        try {
            new BigDecimal(precoField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço deve ser um valor numérico válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            precoField.requestFocus();
            return false;
        }
        
        try {
            Integer.parseInt(estoqueField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Estoque deve ser um número inteiro válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            estoqueField.requestFocus();
            return false;
        }
        
        return true;
    }
    
     
    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0);
        for (Livro livro : livros) {
            Object[] row = new Object[7];
            row[0] = livro.getIsbn();
            row[1] = livro.getTituloAutor();
            row[2] = livro.getGenero();
            row[3] = livro.getAnoPublicacao();
            row[4] = "R$ " + String.format("%.2f", livro.getPreco());
            row[5] = livro.getEstoque();
            
             
            String nomeEditora = "";
            for (int i = 0; i < editoraCombo.getItemCount(); i++) {
                Editora editora = editoraCombo.getItemAt(i);
                if (editora.getId().equals(livro.getIdEditora())) {
                    nomeEditora = editora.getNome();
                    break;
                }
            }
            row[6] = nomeEditora;
            
            tableModel.addRow(row);
        }
    }
    
     
    public void refreshData() {
        try {
            List<Livro> livros = livroDAO.findAll();
            atualizarTabela(livros);
            carregarEditoras();  
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}


