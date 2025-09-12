package br.uepa.livraria.ui;

import br.uepa.livraria.model.dao.CompraDAO;
import br.uepa.livraria.persistence.DAOFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

 
public class RelatorioPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private CompraDAO compraDAO;
    private JTable relatorioTable;
    private DefaultTableModel tableModel;
    private JButton btnTop5Livros;
    private JButton btnAtualizar;
    private JLabel tituloRelatorio;
    
     
    public RelatorioPanel() {
        compraDAO = DAOFactory.createCompraDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
     
    private void initializeComponents() {
         
        tituloRelatorio = new JLabel("Top 5 Livros Mais Vendidos");
        tituloRelatorio.setFont(tituloRelatorio.getFont().deriveFont(Font.BOLD, 16f));
        tituloRelatorio.setHorizontalAlignment(SwingConstants.CENTER);
        
         
        btnTop5Livros = new JButton("Top 5 Livros Mais Vendidos");
        btnTop5Livros.setPreferredSize(new Dimension(250, 30));
        btnAtualizar = new JButton("Atualizar");
        
         
        String[] colunas = {"Posição", "Título/Autor", "Total de Vendas"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  
            }
        };
        relatorioTable = new JTable(tableModel);
        relatorioTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
         
        relatorioTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        relatorioTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        relatorioTable.getColumnModel().getColumn(2).setPreferredWidth(150);
    }
    
     
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
         
        JPanel topPanel = createTopPanel();
        
         
        JScrollPane scrollPane = new JScrollPane(relatorioTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Relatório"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
     
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
         
        panel.add(tituloRelatorio, BorderLayout.NORTH);
        
         
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnTop5Livros);
        buttonPanel.add(btnAtualizar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
     
    private void setupEventHandlers() {
        btnTop5Livros.addActionListener(e -> gerarTop5Livros());
        btnAtualizar.addActionListener(e -> gerarTop5Livros());
    }
    
     
    private void gerarTop5Livros() {
        try {
            List<Object[]> resultados = compraDAO.findTop5LivrosMaisVendidos();
            atualizarTabelaTop5(resultados);
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Nenhuma venda encontrada para gerar o relatório.", 
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao gerar relatório: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void atualizarTabelaTop5(List<Object[]> resultados) {
        tableModel.setRowCount(0);
        
        int posicao = 1;
        for (Object[] resultado : resultados) {
            Object[] row = new Object[3];
            row[0] = posicao + "º";
            row[1] = resultado[0];  
            row[2] = resultado[1];  
            tableModel.addRow(row);
            posicao++;
        }
        
         
        while (posicao <= 5) {
            Object[] row = new Object[3];
            row[0] = posicao + "º";
            row[1] = "N/A";
            row[2] = "0";
            tableModel.addRow(row);
            posicao++;
        }
    }
    
     
    public void refreshData() {
        gerarTop5Livros();
    }
}
