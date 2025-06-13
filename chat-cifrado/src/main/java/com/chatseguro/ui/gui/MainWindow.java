package com.chatseguro.ui.gui;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static final String MENU= "menu";
    public static final String CHAT = "chat";
    public static final String SERVIDOR= "servidor";
    public static final String CLIENTE = "cliente";

    public MainWindow(){
        setTitle("Chat Seguro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MenuPanel menuPanel = new MenuPanel(this);
        menuPanel.setName(MENU);
        mainPanel.add(menuPanel, MENU);

        ServidorPanel servidorPanel = new ServidorPanel(this);
        servidorPanel.setName(SERVIDOR);
        mainPanel.add(servidorPanel, SERVIDOR);

        ClientePanel clientePanel = new ClientePanel(this);
        clientePanel.setName(CLIENTE);
        mainPanel.add(clientePanel, CLIENTE);

        ChatPanel chatPanel = new ChatPanel(this);
        chatPanel.setName(CHAT);
        mainPanel.add(chatPanel, CHAT);
        add(mainPanel);
    }

    public JPanel getPanel(String nombre) {
    for (Component comp : mainPanel.getComponents()) {
        if (mainPanel.getLayout() instanceof CardLayout) {
            if (comp.getName() != null && comp.getName().equals(nombre)) {
                return (JPanel) comp;
            }
        }
    }
    return null;
}

        public void mostrarPanel(String nombrePanel){
            cardLayout.show(mainPanel, nombrePanel);
        }

        public void agregarPanel(JPanel panel, String nombre){
            mainPanel.add(panel,nombre);
        }

        public void iniciar(){
            setVisible(true);
        }
    }


