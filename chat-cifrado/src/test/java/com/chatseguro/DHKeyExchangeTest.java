package com.chatseguro;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

import com.chatseguro.crypto.DHKeyExchange;

public class DHKeyExchangeTest {

    @Test
    public void testKeyExchange() throws Exception {
        DHKeyExchange usuarioA = new DHKeyExchange();
        DHKeyExchange usuarioB = new DHKeyExchange();

        usuarioA.doPhase(usuarioB.getPublicKeyEncoded());
        usuarioB.doPhase(usuarioA.getPublicKeyEncoded());

        byte[] keyA = usuarioA.generateAESKey().getEncoded();
        byte[] keyB = usuarioB.generateAESKey().getEncoded();

        assertArrayEquals("Las claves generadas deben ser iguales", keyA, keyB);
    }
    
}
