/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.app;

/**
 *
 * @author arang
 */
public class ChatApp {
    public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(() -> {
            new com.chatseguro.ui.gui.MainWindow().setVisible(true);
        });
    }
}
