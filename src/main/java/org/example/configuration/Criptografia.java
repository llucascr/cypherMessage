package org.example.configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Criptografia {

    private static final String secretKey = System.getenv("SECRET_KEY");
    private static final String initVector = System.getenv("INIT_VECTOR");

    public Criptografia() {}

    public String encrypt(String password) throws Exception {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public String decrypt(String encrypted) throws Exception {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
            byte[] original = cipher.doFinal(decodedBytes);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
