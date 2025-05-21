package crypto;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DHKeyExchange {
	
	private KeyPair keyPair;
    private SecretKey aesKey;

    public void generarClavesLocales() throws Exception {
        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
        paramGen.init(2048); 
        AlgorithmParameters params = paramGen.generateParameters();
        DHParameterSpec dhSpec = params.getParameterSpec(DHParameterSpec.class);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
        keyGen.initialize(dhSpec);
        keyPair = keyGen.generateKeyPair();
    }

    public byte[] obtenerClavePublica() {
        return keyPair.getPublic().getEncoded();
    }

    public void generarClaveCompartida(byte[] clavePublicaRemota) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(clavePublicaRemota);
        PublicKey publicKeyRemoto = keyFactory.generatePublic(x509Spec);

        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(keyPair.getPrivate());
        keyAgree.doPhase(publicKeyRemoto, true);

        byte[] claveCompartida = keyAgree.generateSecret();
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(claveCompartida);
        aesKey = new SecretKeySpec(hash, 0, 32, "AES"); // 256 bits
    }

    public SecretKey getAESKey() {
        return aesKey;
    }

}
