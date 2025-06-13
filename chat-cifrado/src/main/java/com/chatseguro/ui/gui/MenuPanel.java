package com.chatseguro.ui.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPanel extends JPanel {
    
    @SuppressWarnings("static-access")
    public MenuPanel(MainWindow mainWindow){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Chat Seguro", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(titulo, gbc);

        JButton btnServidor = new JButton("Iniciar como Servidor");
        gbc.gridy = 1;
        add(btnServidor, gbc);

        JButton btnCliente = new JButton("Iniciar como Cliente");
        gbc.gridy = 2;
        add(btnCliente, gbc);

        JButton btnSalir = new JButton("Salir");
        gbc.gridy = 3;
        add(btnSalir, gbc);

        btnServidor.addActionListener((ActionEvent e)-> mainWindow.mostrarPanel(mainWindow.SERVIDOR));
        btnCliente.addActionListener((ActionEvent e)-> mainWindow.mostrarPanel(mainWindow.CLIENTE));
        btnSalir.addActionListener((ActionEvent e) -> System.exit(0));
        
    }

}
