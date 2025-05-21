package crypto;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESManager {

	private SecretKey clave;
    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";

    public AESManager(SecretKey clave) {
        this.clave = clave;
    }

    public String cifrar(String mensaje) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        byte[] iv = new byte[16]; // AES block size
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, clave, ivSpec);
        byte[] cifrado = cipher.doFinal(mensaje.getBytes("UTF-8"));

        // Concatenamos IV + mensaje cifrado
        byte[] resultado = new byte[iv.length + cifrado.length];
        System.arraycopy(iv, 0, resultado, 0, iv.length);
        System.arraycopy(cifrado, 0, resultado, iv.length, cifrado.length);

        return Base64.getEncoder().encodeToString(resultado);
    }

    public String descifrar(String mensajeCifrado) throws Exception {
        byte[] datos = Base64.getDecoder().decode(mensajeCifrado);
        byte[] iv = new byte[16];
        byte[] cifrado = new byte[datos.length - 16];

        System.arraycopy(datos, 0, iv, 0, 16);
        System.arraycopy(datos, 16, cifrado, 0, cifrado.length);

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, clave, new IvParameterSpec(iv));
        byte[] original = cipher.doFinal(cifrado);
        return new String(original, "UTF-8");
    }
}
