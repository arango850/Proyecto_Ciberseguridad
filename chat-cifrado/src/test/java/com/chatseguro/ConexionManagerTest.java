package com.chatseguro;

import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.chatseguro.net.ConexionManager;
import com.chatseguro.net.MensajeHandler;

public class ConexionManagerTest {

    @Test
    public void testConexionClienteServidorYEnvioMensaje() throws Exception {
        int puerto = 5001; // Puedes cambiarlo si el puerto está ocupado
        var executor = Executors.newSingleThreadExecutor();

        // Servidor
        executor.submit(() -> {
            try {
                ConexionManager servidor = new ConexionManager();
                servidor.iniciarComoServidor(puerto);

                MensajeHandler handlerServidor = servidor.getHandler();
                String mensaje = handlerServidor.recibirMensaje();
                assertEquals("Hola desde el cliente", mensaje);

                handlerServidor.enviarMensaje("Hola cliente");
            } catch (Exception e) {
                e.printStackTrace();
                assert false : "Error en el servidor";
            }
        });

        // Espera breve para que el servidor arranque
        Thread.sleep(500);

        // Cliente
        ConexionManager cliente = new ConexionManager();
        cliente.iniciarComoCliente("localhost", puerto);

        MensajeHandler handlerCliente = cliente.getHandler();
        assertNotNull("El handler del cliente no debe ser nulo", handlerCliente);

        handlerCliente.enviarMensaje("Hola desde el cliente");
        String respuesta = handlerCliente.recibirMensaje();

        assertEquals("Hola cliente", respuesta, "El mensaje del servidor debe ser correcto");

        executor.shutdown();
    }
    
}
