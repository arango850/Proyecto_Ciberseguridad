package com.chatseguro.ui.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.chatseguro.net.ConexionManager;

public class ClientePanel extends JPanel {

    private JLabel lblEstado;
    private JTextField txtHost;
    private JTextField txtPuerto;
    private JButton btnConectar;
    private JButton btnVolver;

    public ClientePanel(MainWindow mainWindow){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Modo Cliente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Host/IP:"), gbc);

        txtHost = new JTextField("127.0.0.1");
        gbc.gridx = 1;
        add(txtHost, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Puerto:"), gbc);

        txtPuerto = new JTextField("5000");
        gbc.gridx = 1;
        add(txtPuerto, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnConectar = new JButton("Conectar");
        add(btnConectar, gbc);

        btnVolver = new JButton("Volver");
        gbc.gridx = 1;
        add(btnVolver, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        lblEstado = new JLabel("Estado: Esperando datos...");
        gbc.gridwidth = 2;
        add(lblEstado, gbc);

        btnVolver.addActionListener((ActionEvent e) -> mainWindow.mostrarPanel(MainWindow.MENU));

        btnConectar.addActionListener((ActionEvent e) -> {
            String host = txtHost.getText().trim();
            int puerto;
            try {
                puerto = Integer.parseInt(txtPuerto.getText().trim());
            } catch (NumberFormatException ex) {
                lblEstado.setText("Error: Puerto inválido");
                return;
            }
            lblEstado.setText("Conectando a " + host + ":" + puerto + " ...");
            btnConectar.setEnabled(false);

            new Thread(() -> {
                try {
                    ConexionManager conexion = new com.chatseguro.net.ConexionManager();
                    conexion.iniciarComoCliente(host, puerto);

                    SwingUtilities.invokeLater(() -> {
                        lblEstado.setText("Conectado a " + host + ":" + puerto);
                        ChatPanel chatPanel = (ChatPanel) mainWindow.getPanel(MainWindow.CHAT);
                        chatPanel.setHandler(conexion.getHandler());
                        mainWindow.mostrarPanel(MainWindow.CHAT);
                        
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        lblEstado.setText("Error: " + ex.getMessage());
                        btnConectar.setEnabled(true);
                    });
                }
            }).start();
        });
    }

    public String getHost() {
        return txtHost.getText().trim();
    }

    public int getPuerto() {
        return Integer.parseInt(txtPuerto.getText().trim());
    }

    public void setEstado(String estado) {
        lblEstado.setText(estado);
    }

}
