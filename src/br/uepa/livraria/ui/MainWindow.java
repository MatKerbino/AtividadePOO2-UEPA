package br.uepa.livraria.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

 
public class MainWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
     
    private ClientePanel clientePanel;
    private LivroPanel livroPanel;
    private EditoraPanel editoraPanel;
    private VendaPanel vendaPanel;
    private RelatorioPanel relatorioPanel;
    
     
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
     
    public MainWindow() {
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Gerenciamento de Livraria");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
     
    private void initializeComponents() {
        clientePanel = new ClientePanel();
        livroPanel = new LivroPanel();
        editoraPanel = new EditoraPanel();
        vendaPanel = new VendaPanel();
        relatorioPanel = new RelatorioPanel();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
    }
    
     
    private void setupLayout() {
        setLayout(new BorderLayout());
        
         
        mainPanel.add(clientePanel, "CLIENTE");
        mainPanel.add(livroPanel, "LIVRO");
        mainPanel.add(editoraPanel, "EDITORA");
        mainPanel.add(vendaPanel, "VENDA");
        mainPanel.add(relatorioPanel, "RELATORIO");
        
        add(mainPanel, BorderLayout.CENTER);
        
         
        cardLayout.show(mainPanel, "CLIENTE");
    }
    
     
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
         
        JMenu cadastrosMenu = new JMenu("Cadastros");
        cadastrosMenu.setMnemonic('C');
        
        JMenuItem clienteMenuItem = new JMenuItem("Clientes");
        clienteMenuItem.setMnemonic('C');
        clienteMenuItem.addActionListener(e -> showPanel("CLIENTE"));
        
        JMenuItem livroMenuItem = new JMenuItem("Livros");
        livroMenuItem.setMnemonic('L');
        livroMenuItem.addActionListener(e -> showPanel("LIVRO"));
        
        JMenuItem editoraMenuItem = new JMenuItem("Editoras");
        editoraMenuItem.setMnemonic('E');
        editoraMenuItem.addActionListener(e -> showPanel("EDITORA"));
        
        cadastrosMenu.add(clienteMenuItem);
        cadastrosMenu.add(livroMenuItem);
        cadastrosMenu.add(editoraMenuItem);
        
         
        JMenu vendasMenu = new JMenu("Vendas");
        vendasMenu.setMnemonic('V');
        
        JMenuItem vendaMenuItem = new JMenuItem("Nova Venda");
        vendaMenuItem.setMnemonic('N');
        vendaMenuItem.addActionListener(e -> showPanel("VENDA"));
        
        vendasMenu.add(vendaMenuItem);
        
         
        JMenu relatoriosMenu = new JMenu("Relatórios");
        relatoriosMenu.setMnemonic('R');
        
        JMenuItem relatorioMenuItem = new JMenuItem("Relatórios");
        relatorioMenuItem.setMnemonic('R');
        relatorioMenuItem.addActionListener(e -> showPanel("RELATORIO"));
        
        relatoriosMenu.add(relatorioMenuItem);
        
         
        menuBar.add(cadastrosMenu);
        menuBar.add(vendasMenu);
        menuBar.add(relatoriosMenu);
        
        setJMenuBar(menuBar);
    }
    
     
    private void setupEventHandlers() {
         
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(
                    MainWindow.this,
                    "Deseja realmente sair da aplicação?",
                    "Confirmação de Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
    
     
    private void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        
         
        switch (panelName) {
            case "CLIENTE":
                clientePanel.refreshData();
                break;
            case "LIVRO":
                livroPanel.refreshData();
                break;
            case "EDITORA":
                editoraPanel.refreshData();
                break;
            case "VENDA":
                vendaPanel.refreshData();
                break;
            case "RELATORIO":
                relatorioPanel.refreshData();
                break;
        }
    }
    
     
    public static void main(String[] args) {
         
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
         
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}


