/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.chatseguro.crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author arang
 */
public class DHKeyExchange {

    private KeyPair keyPair;
    private KeyAgreement keyAgree;
    private byte[] sharedSecret;

    private static final BigInteger p = new BigInteger(
        "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1" +
        "29024E088A67CC74020BBEA63B139B22514A08798E3404DD" +
        "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
        "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
        "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE65381" +
        "FFFFFFFFFFFFFFFF", 16);

    private static final BigInteger g = BigInteger.valueOf(2);
    
    public DHKeyExchange() throws  Exception {
        DHParameterSpec dhSpec = new DHParameterSpec(p, g);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH");
        kpg.initialize(dhSpec);
        this.keyPair = kpg.generateKeyPair();
        this.keyAgree = KeyAgreement.getInstance("DH");
        this.keyAgree.init(this.keyPair.getPrivate());
    }

    public byte[] getPublicKeyEncoded(){
        return keyPair.getPublic().getEncoded();
    }

    public void doPhase(byte[] receivedPublicKey) throws Exception{
        KeyFactory kf = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(receivedPublicKey);
        PublicKey receivedKey = kf.generatePublic(x509KeySpec);
        keyAgree.doPhase(receivedKey, true);
        this.sharedSecret= keyAgree.generateSecret();
    }

    public SecretKeySpec generateAESKey(){
        byte [] aesKeyBytes = new byte[32];
        System.arraycopy(sharedSecret, 0, aesKeyBytes, 0, 32);
        return new SecretKeySpec(aesKeyBytes, "AES");
    }

}
