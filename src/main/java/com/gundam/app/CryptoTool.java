package com.gundam.app;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class CryptoTool {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String KEY = "16ByteSecretKey!"; // Must be 16, 24, or 32 bytes for AES

    // Encrypt the properties file
    public static void encryptFile(String inputFile, String outputFile) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] inputBytes = fis.readAllBytes();
            byte[] encryptedBytes = cipher.doFinal(inputBytes);
            fos.write(encryptedBytes);
        }
    }

    // Decrypt the properties file
    public static void decryptFile(String inputFile, String outputFile) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] encryptedBytes = fis.readAllBytes();
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            fos.write(decryptedBytes);
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage:");
            System.out.println("  To encrypt:   java CryptoTool -e <infile> <outfile>");
            System.out.println("  To decrypt:   java CryptoTool -d <infile> <outfile>");
            System.exit(1);
        }

        String mode = args[0];
        String infile = args[1];
        String outfile = args[2];

        try {
            if ("-e".equalsIgnoreCase(mode)) {
                encryptFile(infile, outfile);
                System.out.println("File encrypted successfully: " + outfile);
            } else if ("-d".equalsIgnoreCase(mode)) {
                decryptFile(infile, outfile);
                System.out.println("File decrypted successfully: " + outfile);
            } else {
                System.out.println("Invalid mode. Use -e to encrypt or -d to decrypt.");
                System.exit(1);
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }
}