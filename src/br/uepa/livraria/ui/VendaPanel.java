package br.uepa.livraria.ui;

import br.uepa.livraria.model.dao.CompraDAO;
import br.uepa.livraria.model.dao.LivroDAO;
import br.uepa.livraria.model.dao.ClienteDAO;
import br.uepa.livraria.model.entities.Compra;
import br.uepa.livraria.model.entities.Livro;
import br.uepa.livraria.model.entities.Cliente;
import br.uepa.livraria.persistence.DAOFactory;
import br.uepa.livraria.persistence.impl.CompraDAOJDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VendaPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private CompraDAO compraDAO;
    private LivroDAO livroDAO;
    private ClienteDAO clienteDAO;
    private JTable vendaTable;
    private DefaultTableModel tableModel;
    private JComboBox<Cliente> clienteCombo;
    private JComboBox<Livro> livroCombo;
    private JTextField quantidadeField;
    private JLabel precoUnitarioLabel;
    private JLabel valorTotalLabel;
    private JButton btnInserir;
    private JButton btnLimpar;
    private JButton btnBuscar;
    private JTextField buscarField;
    private JComboBox<String> tipoBuscaCombo;
    
    public VendaPanel() {
        compraDAO = DAOFactory.createCompraDAO();
        livroDAO = DAOFactory.createLivroDAO();
        clienteDAO = DAOFactory.createClienteDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
    private void initializeComponents() {
        clienteCombo = new JComboBox<>();
        clienteCombo.setPreferredSize(new Dimension(500, 35));
        
        livroCombo = new JComboBox<>();
        livroCombo.setPreferredSize(new Dimension(500, 35));
        livroCombo.addActionListener(e -> atualizarPrecoUnitario());
        
        quantidadeField = new JTextField(25);
        quantidadeField.setPreferredSize(new Dimension(300, 35));
        quantidadeField.addActionListener(e -> calcularValorTotal());
        
        precoUnitarioLabel = new JLabel("R$ 0,00");
        precoUnitarioLabel.setFont(precoUnitarioLabel.getFont().deriveFont(Font.BOLD));
        
        valorTotalLabel = new JLabel("R$ 0,00");
        valorTotalLabel.setFont(valorTotalLabel.getFont().deriveFont(Font.BOLD, 16f));
        valorTotalLabel.setForeground(Color.BLUE);
        
        btnInserir = new JButton("Registrar Venda");
        btnInserir.setPreferredSize(new Dimension(200, 40));
        btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(120, 40));
        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(120, 40));
        
        buscarField = new JTextField(30);
        buscarField.setPreferredSize(new Dimension(400, 35));
        tipoBuscaCombo = new JComboBox<>(new String[]{"Cliente", "Livro"});
        tipoBuscaCombo.setPreferredSize(new Dimension(150, 35));
        
        String[] colunas = {"ID", "Cliente", "Livro", "Quantidade", "Preço Unit.", "Valor Total", "Data"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vendaTable = new JTable(tableModel);
        vendaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        carregarClientes();
        carregarLivros();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = createFormPanel();
        
        JScrollPane scrollPane = new JScrollPane(vendaTable);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        
        JPanel searchPanel = createSearchPanel();
        
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nova Venda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Primeira linha: Cliente e Livro
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(clienteCombo, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Livro:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(livroCombo, gbc);
        
        // Segunda linha: Quantidade e Preço Unitário
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(quantidadeField, gbc);
        
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Preço Unitário:"), gbc);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(precoUnitarioLabel, gbc);
        
        // Terceira linha: Valor Total
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Valor Total:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(valorTotalLabel, gbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnInserir);
        buttonPanel.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Vendas"));
        panel.add(new JLabel("Tipo:"));
        panel.add(tipoBuscaCombo);
        panel.add(new JLabel("Termo:"));
        panel.add(buscarField);
        panel.add(btnBuscar);
        return panel;
    }
    
    private void setupEventHandlers() {
        btnInserir.addActionListener(e -> registrarVenda());
        btnLimpar.addActionListener(e -> limparCampos());
        btnBuscar.addActionListener(e -> buscarVendas());
    }
    
    private void carregarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.findAll();
            clienteCombo.removeAllItems();
            for (Cliente cliente : clientes) {
                clienteCombo.addItem(cliente);
            }
            
            // Configurar renderer customizado para mostrar apenas o nome
            clienteCombo.setRenderer(new javax.swing.ListCellRenderer<Cliente>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Cliente> list, Cliente value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = new JLabel();
                    if (value != null) {
                        label.setText(value.getNome() + " (" + value.getTipoCliente() + ")");
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
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarLivros() {
        try {
            List<Livro> livros = livroDAO.findAll();
            livroCombo.removeAllItems();
            for (Livro livro : livros) {
                if (livro.getEstoque() > 0) {
                    livroCombo.addItem(livro);
                }
            }
            
            // Configurar renderer customizado para mostrar apenas o título/autor
            livroCombo.setRenderer(new javax.swing.ListCellRenderer<Livro>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Livro> list, Livro value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = new JLabel();
                    if (value != null) {
                        label.setText(value.getTituloAutor() + " - R$ " + String.format("%.2f", value.getPreco()) + " (Estoque: " + value.getEstoque() + ")");
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
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarPrecoUnitario() {
        Livro livro = (Livro) livroCombo.getSelectedItem();
        if (livro != null) {
            precoUnitarioLabel.setText("R$ " + String.format("%.2f", livro.getPreco()));
            calcularValorTotal();
        } else {
            precoUnitarioLabel.setText("R$ 0,00");
            valorTotalLabel.setText("R$ 0,00");
        }
    }
    
    private void calcularValorTotal() {
        Livro livro = (Livro) livroCombo.getSelectedItem();
        String quantidadeText = quantidadeField.getText().trim();
        
        if (livro != null && !quantidadeText.isEmpty()) {
            try {
                int quantidade = Integer.parseInt(quantidadeText);
                BigDecimal valorTotal = livro.getPreco().multiply(BigDecimal.valueOf(quantidade));
                valorTotalLabel.setText("R$ " + String.format("%.2f", valorTotal));
            } catch (NumberFormatException e) {
                valorTotalLabel.setText("R$ 0,00");
            }
        } else {
            valorTotalLabel.setText("R$ 0,00");
        }
    }
    
    private void registrarVenda() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            Cliente cliente = (Cliente) clienteCombo.getSelectedItem();
            Livro livro = (Livro) livroCombo.getSelectedItem();
            int quantidade = Integer.parseInt(quantidadeField.getText().trim());
            
            if (livro.getEstoque() < quantidade) {
                JOptionPane.showMessageDialog(this, 
                    "Estoque insuficiente! Disponível: " + livro.getEstoque() + " unidades", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Compra compra = new Compra();
            compra.setIsbnLivro(livro.getIsbn());
            compra.setIdCliente(cliente.getId());
            compra.setQuantidade(quantidade);
            compra.setPrecoUnitario(livro.getPreco());
            compra.setDataCompra(LocalDateTime.now());
            
            if (compraDAO instanceof CompraDAOJDBC) {
                ((CompraDAOJDBC) compraDAO).insertComTransacao(compra, quantidade);
            } else {
                compraDAO.insert(compra);
                livroDAO.decrementarEstoque(livro.getIsbn(), quantidade);
            }
            
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            refreshData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        clienteCombo.setSelectedIndex(0);
        livroCombo.setSelectedIndex(0);
        quantidadeField.setText("");
        precoUnitarioLabel.setText("R$ 0,00");
        valorTotalLabel.setText("R$ 0,00");
    }
    
    private void buscarVendas() {
        String termo = buscarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                String tipo = (String) tipoBuscaCombo.getSelectedItem();
                List<Compra> vendas;
                
                if ("Cliente".equals(tipo)) {
                    vendas = compraDAO.findAll();
                } else {
                    vendas = compraDAO.findByLivro(termo);
                }
                
                atualizarTabela(vendas);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar vendas: " + e.getMessage(), 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validarCampos() {
        if (clienteCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente!", "Validação", JOptionPane.WARNING_MESSAGE);
            clienteCombo.requestFocus();
            return false;
        }
        
        if (livroCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro!", "Validação", JOptionPane.WARNING_MESSAGE);
            livroCombo.requestFocus();
            return false;
        }
        
        if (quantidadeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantidade é obrigatória!", "Validação", JOptionPane.WARNING_MESSAGE);
            quantidadeField.requestFocus();
            return false;
        }
        
        try {
            int quantidade = Integer.parseInt(quantidadeField.getText().trim());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero!", "Validação", JOptionPane.WARNING_MESSAGE);
                quantidadeField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            quantidadeField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void atualizarTabela(List<Compra> vendas) {
        tableModel.setRowCount(0);
        for (Compra venda : vendas) {
            Object[] row = new Object[7];
            row[0] = venda.getId();
            
            String nomeCliente = "";
            for (int i = 0; i < clienteCombo.getItemCount(); i++) {
                Cliente cliente = clienteCombo.getItemAt(i);
                if (cliente.getId().equals(venda.getIdCliente())) {
                    nomeCliente = cliente.getNome();
                    break;
                }
            }
            row[1] = nomeCliente;
            
            String tituloLivro = "";
            for (int i = 0; i < livroCombo.getItemCount(); i++) {
                Livro livro = livroCombo.getItemAt(i);
                if (livro.getIsbn().equals(venda.getIsbnLivro())) {
                    tituloLivro = livro.getTituloAutor();
                    break;
                }
            }
            row[2] = tituloLivro;
            
            row[3] = venda.getQuantidade();
            row[4] = "R$ " + String.format("%.2f", venda.getPrecoUnitario());
            row[5] = "R$ " + String.format("%.2f", venda.getValorTotal());
            row[6] = venda.getDataCompra().toString();
            
            tableModel.addRow(row);
        }
    }

    public void refreshData() {
        try {
            List<Compra> vendas = compraDAO.findAll();
            atualizarTabela(vendas);
            carregarClientes();
            carregarLivros();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vendas: " + e.getMessage(), 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
