package com.chatseguro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.chatseguro.ui.ConsolaUI;

public class ConsolaUITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    private ConsolaUI consolaUI;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        consolaUI = new ConsolaUI();
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    public void testMostrarMenuPrincipal() {
        String input = "2\n";  // simulamos elegir "Cliente"
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int opcion = consolaUI.mostrarMenuPrincipal();
        assertEquals(2, opcion);
        assertTrue(outContent.toString().contains("1. Iniciar como Servidor"));
    }

    @Test
    public void testPedirHost() {
        String input = "localhost\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String host = consolaUI.pedirHost();
        assertEquals("localhost", host);
    }

    @Test
    public void testPedirPuerto() {
        String input = "5000\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int puerto = consolaUI.pedirPuerto();
        assertEquals(5000, puerto);
    }

    @Test
    public void testLeerMensaje() {
        String input = "mensaje de prueba\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String mensaje = consolaUI.leerMensaje();
        assertEquals("mensaje de prueba", mensaje);
    }

    @Test
    public void testMostrarMensaje() {
        consolaUI.mostrarMensaje("Hola");
        assertTrue(outContent.toString().contains("Remoto: Hola"));
    }

    @Test
    public void testMostrarError() {
        consolaUI.mostrarError("Conexión fallida");
        assertTrue(errContent.toString().contains("ERROR: Conexión fallida"));
    }
    
}
