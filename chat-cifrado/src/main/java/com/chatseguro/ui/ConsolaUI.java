/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.ui;

import java.util.Scanner;

/**
 *
 * @author arang
 */
public class ConsolaUI {

    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenuPrincipal(){
        System.out.println("======== Chat Seguro ========");
        System.out.println("1. Iniciar como Servidor");
        System.out.println("2. Iniciar como Cliente");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public String pedirHost() {
        System.out.print("Ingrese la IP o hostname del servidor: ");
        return scanner.next();
    }

    public int pedirPuerto() {
        System.out.print("Ingrese el puerto: ");
        return scanner.nextInt();
    }

    public String leerMensaje() {
        System.out.print("Tú: ");
        scanner.nextLine(); // limpiar buffer
        return scanner.nextLine();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("Remoto: " + mensaje);
    }

    public void mostrarError(String msg) {
        System.err.println("ERROR: " + msg);
    }

}
