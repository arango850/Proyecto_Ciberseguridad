package com.chatseguro.ui.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Handler;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.chatseguro.net.ConexionManager;
import com.chatseguro.net.MensajeHandler;

public class ChatPanel extends JPanel {

    private JTextArea areaMensajes;
    private JTextField campoMensaje;
    private JButton btnEnviar;
    private JButton btnSalir;
    private JLabel lblEstado;

    private MensajeHandler handler;

    public ChatPanel(MainWindow mainWindow){
        setLayout(new BorderLayout(10, 10));

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaMensajes);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        campoMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        btnSalir = new JButton("Salir del chat");

        panelInferior.add(campoMensaje, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);
        panelInferior.add(btnSalir, BorderLayout.WEST);

        lblEstado = new JLabel("Conectado", SwingConstants.LEFT);

        add(lblEstado, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnEnviar.addActionListener((ActionEvent e) -> enviarMensaje());
        campoMensaje.addActionListener((ActionEvent e) -> enviarMensaje());

        btnSalir.addActionListener((ActionEvent e) -> {
            if (handler != null) {
                try { handler.cerrar(); } catch (Exception ex) {}
            }
            mainWindow.mostrarPanel(MainWindow.MENU);
            limpiarChat();
        });
    }

    public void agregarMensaje(String mensaje, boolean propio) {
        if (propio) {
            areaMensajes.append("Tú: " + mensaje + "\n");
        } else {
            areaMensajes.append("Remoto: " + mensaje + "\n");
        }
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }

    public void limpiarChat() {
        areaMensajes.setText("");
        campoMensaje.setText("");
        lblEstado.setText("Conectado");
    }

    public void setEstado(String estado) {
        lblEstado.setText(estado);
    }

    private void enviarMensaje() {
        String mensaje = campoMensaje.getText().trim();
        if (!mensaje.isEmpty() && handler != null) {
            try {
                handler.enviarMensaje(mensaje);
                agregarMensaje(mensaje, true);
                campoMensaje.setText("");
            } catch (Exception e) {
                setEstado("Error al enviar mensaje");
            }
        }
    } 

    

    public void setHandler(com.chatseguro.net.MensajeHandler handler) {
        this.handler = handler;
    
        new Thread(() -> {
            try {
                while (true) {
                    String recibido = handler.recibirMensaje();
                    SwingUtilities.invokeLater(() -> agregarMensaje(recibido, false));
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> setEstado("Conexión cerrada"));
            }
        }).start();
    }


}
