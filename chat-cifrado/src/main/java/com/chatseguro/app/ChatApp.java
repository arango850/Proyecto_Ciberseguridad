/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.app;

import com.chatseguro.net.ConexionManager;
import com.chatseguro.net.MensajeHandler;
import com.chatseguro.ui.ConsolaUI;

/**
 *
 * @author arang
 */
public class ChatApp {
    public static void main(String[] args) {
    ConsolaUI ui= new ConsolaUI();
    ConexionManager conexion = new ConexionManager();
    
    
    
    try{
        int opcion = ui.mostrarMenuPrincipal();

        switch(opcion){
            case 1 ->{
                int puerto = ui.pedirPuerto();
                conexion.iniciarComoServidor(puerto);
            }
            case 2 ->{
                String host = ui.pedirHost();
                int puerto = ui.pedirPuerto();
                conexion.iniciarComoCliente(host, puerto);
            }
            case 3 ->{
                System.out.println("Saliendo de la aplicación...");
                return;
            }
            default -> {
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
                return;
            }
        }

        MensajeHandler handler = conexion.getHandler();

        Thread lector = new Thread(() ->{
            while (true){
                try {
                    String msg = handler.recibirMensaje();
                    ui.mostrarMensaje(msg);
                } catch (Exception e) {
                    ui.mostrarError("Conexion cerrada");
                    break;
                }
            }
        });

        lector.start();

        while (true){
            String mensaje = ui.leerMensaje();
            handler.enviarMensaje(mensaje);
        }
        
    } catch (Exception e){
        ui.mostrarError("Fallo en la aplicación: " + e.getMessage());
        e.printStackTrace();
        }
    } 
}
