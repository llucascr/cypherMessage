package org.example.configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Criptografia {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12; // vetor de inicialização.


    public Criptografia() {}

    /**
     * Deriva uma chave AES de 256 bits a partir de um token de qualquer tamanho.
     *
     * <p>O token é convertido em bytes UTF-8 e processado com o algoritmo SHA-256,
     * garantindo que a saída tenha 32 bytes (256 bits), compatível com AES-256.
     *
     * @param token
     * @return
     * @throws Exception
     */
    private static SecretKeySpec getKeyFromToken(String token) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, ALGORITHM); // 256 bits
    }

    /***
     * Criptografa uma mensagem de texto usando AES em modo GCM (autenticado).
     *
     * <p>Um vetor de inicialização (IV) aleatório de 12 bytes é gerado para cada mensagem,
     * garantindo segurança mesmo quando o mesmo token é reutilizado.
     * O IV é concatenado com o texto criptografado e convertido para Base64.
     *
     * @param plainText
     * @param token
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String token) throws Exception {
        SecretKeySpec key = getKeyFromToken(token);
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        byte[] combined = new byte[IV_LENGTH + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
        System.arraycopy(encrypted, 0, combined, IV_LENGTH, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    /***
     * Descriptografa um texto previamente criptografado com {@link #encrypt(String, String)}.
     *
     * @param encryptedText
     * @param token
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedText, String token) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        SecretKeySpec key = getKeyFromToken(token);

        byte[] iv = Arrays.copyOfRange(decoded, 0, IV_LENGTH);
        byte[] ciphertext = Arrays.copyOfRange(decoded, IV_LENGTH, decoded.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
