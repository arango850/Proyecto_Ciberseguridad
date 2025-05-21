package net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import crypto.AESManager;

public class MensajeHandler implements Runnable{
	
	private Socket socket;
    private AESManager aesManager;

    public MensajeHandler(Socket socket, AESManager aesManager) {
        this.socket = socket;
        this.aesManager = aesManager;
    }

    @Override
    public void run() {
        try (
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner entradaUsuario = new Scanner(System.in)
        ) {
          
            new Thread(() -> {
                try {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        String descifrado = aesManager.descifrar(linea);
                        System.out.println("Amigo: " + descifrado);
                    }
                } catch (Exception e) {
                    System.err.println("Error al recibir: " + e.getMessage());
                }
            }).start();

            
            while (true) {
                String mensaje = entradaUsuario.nextLine();
                String cifrado = aesManager.cifrar(mensaje);
                escritor.write(cifrado);
                escritor.newLine();
                escritor.flush();
            }
        } catch (Exception e) {
            System.err.println("Error en el chat: " + e.getMessage());
        }
    }

}
