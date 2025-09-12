package br.uepa.livraria.application;

import br.uepa.livraria.ui.MainWindow;

 
public class Main {
    
     
    public static void main(String[] args) {
        try {
             
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
        }
        
         
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
                
                System.out.println("Sistema de Gerenciamento de Livraria iniciado com sucesso!");
                
            } catch (Exception e) {
                System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
                e.printStackTrace();
                
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Erro ao iniciar a aplicação:\n" + e.getMessage(),
                    "Erro Fatal",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                
                System.exit(1);
            }
        });
    }
}
