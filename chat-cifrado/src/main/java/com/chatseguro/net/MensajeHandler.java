/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.chatseguro.crypto.AESManager;

/**
 *
 * @author arang
 */
public class MensajeHandler {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private AESManager aesManager;

    public MensajeHandler(Socket socket, AESManager aesManager) throws IOException{
        this.socket=socket;
        this.aesManager= aesManager;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void enviarMensaje(String mensaje) throws Exception{
        String mensajeCifrado = aesManager.encrypt(mensaje);
        out.write(mensajeCifrado);
        out.newLine();
        out.flush();
    }

    public String recibirMensaje() throws Exception{
        String mensajeCifrado = in.readLine();
        if (mensajeCifrado == null){
            throw new IOException("Conexión cerrada por el cliente");
        }
        return aesManager.decrypt(mensajeCifrado);
    }

    public void cerrar() throws  IOException{
        in.close();
        out.close();
        socket.close();
    }
}
