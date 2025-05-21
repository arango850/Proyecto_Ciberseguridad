package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.Logger;

public class ConexionManager {

	private boolean esServidor;
    private String ipServidor;
    private int puerto;

    private ServerSocket serverSocket;
    private Socket socket;

    public ConexionManager(boolean esServidor, String ipServidor, int puerto) {
        this.esServidor = esServidor;
        this.ipServidor = ipServidor;
        this.puerto = puerto;
    }

    public void iniciar() {
        if (esServidor) {
            iniciarComoServidor();
        } else {
            iniciarComoCliente();
        }
    }

    private void iniciarComoServidor() {
        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("Esperando conexión en el puerto " + puerto + "...");
            socket = serverSocket.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress());

            
        } catch (IOException e) {
            Logger.error("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private void iniciarComoCliente() {
        try {
            System.out.println("Conectando al servidor " + ipServidor + ":" + puerto + "...");
            socket = new Socket(ipServidor, puerto);
            System.out.println("Conectado al servidor");

            
        } catch (IOException e) {
            Logger.error("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void cerrarConexion() {
        try {
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            Logger.error("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
