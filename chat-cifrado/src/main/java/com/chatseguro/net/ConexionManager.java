/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.SecretKey;

import com.chatseguro.crypto.AESManager;
import com.chatseguro.crypto.DHKeyExchange;

/**
 *
 * @author arang
 */
public class ConexionManager {

    private Socket socket;
    private MensajeHandler handler;

    public MensajeHandler getHandler() {
        return handler;
    }

    public void iniciarComoServidor(int puerto) throws  Exception{
        ServerSocket serverSocket = new ServerSocket(puerto);
        System.out.println("Esperando conexión en el puerto "+ puerto+"...");
        this.socket = serverSocket.accept();
        System.out.println("Conexión aceptada desde "+ socket.getInetAddress());

        establecerConexionSegura(socket.getInputStream(), socket.getOutputStream());
    }

    public void iniciarComoCliente(String host, int puerto) throws Exception{
        this.socket = new Socket(host,puerto);
        System.out.println("Conectando a "+host+":"+puerto);
        establecerConexionSegura(socket.getInputStream(), socket.getOutputStream());
    }

    private void establecerConexionSegura(InputStream in, OutputStream out) throws  Exception{
        DHKeyExchange dh = new DHKeyExchange();

        out.write(dh.getPublicKeyEncoded().length);
        out.write(dh.getPublicKeyEncoded());
        out.flush();

        int length = in.read();
        byte[] publicKeyReceived = in.readNBytes(length);
        dh.doPhase(publicKeyReceived);

        SecretKey key = dh.generateAESKey();
        AESManager aes = new AESManager(key);

        this.handler = new MensajeHandler(socket, aes);
    }

}
