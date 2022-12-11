package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import static java.lang.System.exit;

public class Crypto {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        if(args[0].equals("checksha") && args.length != 2) throw new IllegalArgumentException("Expected 2 arguments for checksha method!");
        if((args[0].equals("encrypt") || args[0].equals("decrypt")) && args.length != 3) throw new IllegalArgumentException("Expected 3 arguments for encryption/decryption!");
        if(!(args[0].equals("checksha") || args[0].equals("encrypt") || args[0].equals("decrypt"))) throw new IllegalArgumentException("I dont know other than checksha, encrypt, decrypt...");

        switch (args[0]) {
            case "checksha": Checksha(args[1]);
            case "encrypt": Encrypt(args[1], args[2]);
            case "decrypt": Decrypt(args[1], args[2]);
        }
    }

    private static void Decrypt(String arg1, String arg2) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Scanner sc = new Scanner(System.in);

        Path path1 = Paths.get(arg1);
        Path path2 = Paths.get(arg2);

        InputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(path1), 4096);
        OutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(path2), 4096);

        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ");
        System.out.print("> ");
        String keyText = sc.nextLine();

        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits): ");
        System.out.print("> ");
        String ivText = sc.nextLine();

        SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);

        while(bufferedInputStream.available() != 0) {
            byte[] output = cipher.update(bufferedInputStream.readNBytes(4096));
            bufferedOutputStream.write(output);
        }

        byte[] cipherText = cipher.doFinal();

        bufferedOutputStream.write(cipherText);

        System.out.println("Encryption completed. Generated file " + arg1 + " based on file " + arg2);

        bufferedInputStream.close();
        bufferedOutputStream.close();
        exit(0);
    }

    private static void Encrypt(String arg1, String arg2) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Scanner sc = new Scanner(System.in);

        Path path1 = Paths.get(arg1);
        Path path2 = Paths.get(arg2);

        InputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(path1), 4096);
        OutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(path2), 4096);

        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ");
        System.out.print("> ");
        String keyText = sc.nextLine();

        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits): ");
        System.out.print("> ");
        String ivText = sc.nextLine();

        SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);

        while(bufferedInputStream.available() != 0) {
            byte[] output = cipher.update(bufferedInputStream.readNBytes(4096));
            bufferedOutputStream.write(output);
        }

        byte[] cipherText = cipher.doFinal();

        bufferedOutputStream.write(cipherText);

        System.out.println("Encryption completed. Generated file " + arg1 + " based on file " + arg2);

        bufferedInputStream.close();
        bufferedOutputStream.close();
        exit(0);
    }

    private static void Checksha(String arg) throws NoSuchAlgorithmException, IOException {
        Scanner sc = new Scanner(System.in);
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        Path path = Paths.get(arg);
        InputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(path), 4096);

        System.out.println("Please provide expected sha-256 digest for hw05test.bin:");
        System.out.print("> ");
        String shaDigest = sc.nextLine();

        while(bufferedInputStream.available() != 0) {
            md.update(bufferedInputStream.readNBytes(4096));
        }

        byte[] digest = md.digest();
        String digestAsHex = Util.byteToHex(digest);

        if(shaDigest.equals(digestAsHex)) {
            System.out.println("Digesting completed. Digest of " + arg + " matches expected digest.");
        } else {
            System.out.println("Digesting completed. Digest of " + arg + " does not match the expected digest. Digest was: " + digestAsHex);
        }

        bufferedInputStream.close();
        exit(0);
    }
}
