package com.chatseguro.ui.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ServidorPanel extends JPanel {

    private JLabel lblEstado;
    private JLabel lblIP;
    private JLabel lblPuerto;
    private JButton btnIniciar;
    private JButton btnCopiar;
    private JButton btnVolver;
    private JTextField txtPuerto;

    public ServidorPanel(MainWindow mainWindow){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Modo Servidor", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Puerto:"), gbc);

        txtPuerto = new JTextField("5000");
        gbc.gridx = 1;
        add(txtPuerto, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnIniciar = new JButton("Iniciar Servidor");
        add(btnIniciar, gbc);

        btnVolver = new JButton("Volver");
        gbc.gridx = 1;
        add(btnVolver, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        lblIP = new JLabel("IP: -");
        add(lblIP, gbc);

        lblPuerto = new JLabel("Puerto: -");
        gbc.gridx = 1;
        add(lblPuerto, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        lblEstado = new JLabel("Estado: Esperando inicio...");
        gbc.gridwidth = 2;
        add(lblEstado, gbc);

        btnCopiar = new JButton("Copiar IP:Puerto");
        gbc.gridy++;
        add(btnCopiar, gbc);

         btnVolver.addActionListener((ActionEvent e) -> mainWindow.mostrarPanel(MainWindow.MENU));

         btnCopiar.addActionListener((ActionEvent e) -> {
            String info = lblIP.getText().replace("IP: ", "") + ":" + lblPuerto.getText().replace("Puerto: ", "");
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(info), null);
            JOptionPane.showMessageDialog(this, "Copiado al portapapeles: " + info);
        });

        btnIniciar.addActionListener((ActionEvent e) -> {
        try {
        int puerto = Integer.parseInt(txtPuerto.getText().trim());
        String ip = InetAddress.getLocalHost().getHostAddress();
        lblIP.setText("IP: " + ip);
        lblPuerto.setText("Puerto: " + puerto);
        lblEstado.setText("Esperando conexión en " + ip + ":" + puerto + " ...");
        btnIniciar.setEnabled(false);

        new Thread(() -> {
            try {
                com.chatseguro.net.ConexionManager conexion = new com.chatseguro.net.ConexionManager();
                conexion.iniciarComoServidor(puerto);

                
                SwingUtilities.invokeLater(() -> {
                    setClienteConectado(conexion.getHandler().socket.getInetAddress().getHostAddress());
                });

                // Cambiar a la ventana de chat
                SwingUtilities.invokeLater(() -> {
                    ChatPanel chatPanel = (ChatPanel) mainWindow.getPanel(MainWindow.CHAT);
                    chatPanel.setHandler(conexion.getHandler());
                    mainWindow.mostrarPanel(MainWindow.CHAT);
                    // Puedes pasar la conexión/handler al ChatPanel si lo necesitas
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> lblEstado.setText("Error: " + ex.getMessage()));
            }
            }).start();

        } catch (Exception ex) {
        lblEstado.setText("Error: " + ex.getMessage());
            }
        });
    }

    public void setEstado(String estado) {
        lblEstado.setText(estado);
    }

    public void setClienteConectado(String clienteIP) {
        lblEstado.setText("Cliente conectado desde: " + clienteIP);
    }

    public int getPuerto() {
        return Integer.parseInt(txtPuerto.getText().trim());
    }
}
