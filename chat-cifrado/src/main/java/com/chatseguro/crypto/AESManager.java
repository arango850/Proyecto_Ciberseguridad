/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.crypto;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author arang
 */
public class AESManager {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private SecretKey key;
    private IvParameterSpec iv;

    public AESManager(SecretKey key){
        this.key = key;
        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        this.iv= new IvParameterSpec(ivBytes);
    }

    public String encrypt(String plainText) throws Exception{
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryped = cipher.doFinal(plainText.getBytes("UTF-8"));
        byte[] combined = new byte[iv.getIV().length + encryped.length];
        System.arraycopy(iv.getIV(), 0, combined, 0, iv.getIV().length);
        System.arraycopy(encryped, 0, combined, iv.getIV().length, encryped.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String cipherText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(cipherText);
        byte[] ivBytes = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 0, ivBytes, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, "UTF-8");
    }



}
