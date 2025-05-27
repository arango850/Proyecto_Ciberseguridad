package com.chatseguro;

import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.chatseguro.crypto.AESManager;

public class AESManagerTest {
    
    @Test
    public void testEncryptDecrypt() throws Exception {
        byte[] keyBytes = new byte[32]; // 256 bits
        Arrays.fill(keyBytes, (byte) 1); // Clave fija para prueba
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        AESManager aesManager = new AESManager(key);
        String original = "Este es un mensaje secreto";

        String encrypted = aesManager.encrypt(original);
        String decrypted = aesManager.decrypt(encrypted);

        assertEquals("El mensaje descifrado debe coincidir con el original", "Este es un mensaje secreto", decrypted);

    }
}
