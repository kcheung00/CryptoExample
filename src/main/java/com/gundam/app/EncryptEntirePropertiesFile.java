import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EncryptEntirePropertiesFile {
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

    // Decrypt and load the properties file
    public static Properties decryptFile(String encryptedFile) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        try (FileInputStream fis = new FileInputStream(encryptedFile)) {
            byte[] encryptedBytes = fis.readAllBytes();
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            Properties props = new Properties();
            props.load(new ByteArrayInputStream(decryptedBytes));
            return props;
        }
    }

    public static void main(String[] args) throws Exception {
        // Encrypt the file (run once)
        encryptFile("config.properties", "config.encrypted");

        // Decrypt and use the properties
        Properties props = decryptFile("config.encrypted");
        System.out.println("Decrypted Properties: " + props);
    }
}