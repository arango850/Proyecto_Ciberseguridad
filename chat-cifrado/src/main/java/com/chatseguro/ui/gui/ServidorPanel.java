package com.chatseguro.ui.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JComboBox<String> comboIPs;

    public ServidorPanel(MainWindow mainWindow) {
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

        // IPs disponibles
        List<String> ips = obtenerIPsLocales();
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Selecciona la IP:"), gbc);

        comboIPs = new JComboBox<>(ips.toArray(new String[0]));
        gbc.gridx = 1;
        add(comboIPs, gbc);

        gbc.gridx = 0;
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

        // Acción para volver al menú
        btnVolver.addActionListener((ActionEvent e) -> mainWindow.mostrarPanel(MainWindow.MENU));

        // Acción para copiar IP y puerto
        btnCopiar.addActionListener((ActionEvent e) -> {
            String info = lblIP.getText().replace("IP: ", "") + ":" + lblPuerto.getText().replace("Puerto: ", "");
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(info), null);
            JOptionPane.showMessageDialog(this, "Copiado al portapapeles: " + info);
        });

        // Acción para iniciar el servidor
        btnIniciar.addActionListener((ActionEvent e) -> {
            try {
                int puerto = Integer.parseInt(txtPuerto.getText().trim());
                String ipSeleccionada = (String) comboIPs.getSelectedItem();
                lblIP.setText("IP: " + ipSeleccionada);
                lblPuerto.setText("Puerto: " + puerto);
                lblEstado.setText("Esperando conexión en " + ipSeleccionada + ":" + puerto + " ...");
                btnIniciar.setEnabled(false);

                new Thread(() -> {
                    try {
                        com.chatseguro.net.ConexionManager conexion = new com.chatseguro.net.ConexionManager();
                        // Debes tener un método en ConexionManager que acepte IP y puerto:
                        conexion.iniciarComoServidor1(puerto, ipSeleccionada);

                        SwingUtilities.invokeLater(() -> {
                            setClienteConectado(conexion.getHandler().socket.getInetAddress().getHostAddress());
                        });

                        SwingUtilities.invokeLater(() -> {
                            ChatPanel chatPanel = (ChatPanel) mainWindow.getPanel(MainWindow.CHAT);
                            chatPanel.setHandler(conexion.getHandler());
                            mainWindow.mostrarPanel(MainWindow.CHAT);
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

    // Método para actualizar el estado desde la lógica de red
    public void setEstado(String estado) {
        lblEstado.setText(estado);
    }

    public void setClienteConectado(String clienteIP) {
        lblEstado.setText("Cliente conectado desde: " + clienteIP);
    }

    public int getPuerto() {
        return Integer.parseInt(txtPuerto.getText().trim());
    }

    // Método para obtener todas las IPs locales IPv4 no loopback
    private List<String> obtenerIPsLocales() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (addr instanceof java.net.Inet4Address && !addr.isLoopbackAddress()) {
                            ips.add(addr.getHostAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
            ips.add("No se pudieron obtener las IPs");
        }
        return ips;
    }
}
