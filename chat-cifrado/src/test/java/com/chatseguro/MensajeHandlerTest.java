package com.chatseguro;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.chatseguro.crypto.AESManager;
import com.chatseguro.net.MensajeHandler;

public class MensajeHandlerTest {

    @Test
    public void testEnviarYRecibirMensaje() throws Exception {
        byte[] keyBytes = new byte[32];
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        var executor = Executors.newSingleThreadExecutor();

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            // Servidor simulando recepción
            executor.submit(() -> {
                try (Socket socketServidor = serverSocket.accept()) {
                    MensajeHandler receptor = new MensajeHandler(socketServidor, new AESManager(key));
                    String recibido = receptor.recibirMensaje();
                    assertEquals("Hola desde el cliente", recibido);
                } catch (IOException e) {
                    fail("Error en el servidor: " + e.getMessage());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });

            // Cliente
            try (Socket socketCliente = new Socket("localhost", port)) {
                MensajeHandler emisor = new MensajeHandler(socketCliente, new AESManager(key));
                emisor.enviarMensaje("Hola desde el cliente");
            }
        }

        executor.shutdown();
    }

    
    
}
