import java.io.*;
import java.util.Scanner;

interface EncryptionStrategy {
    String encrypt(String text);
}

class RemoveVowelsEncryption implements EncryptionStrategy {
    @Override
    public String encrypt(String text) {
        return text.replaceAll("[aeiouAEIOU]", "");
    }
}

class CaesarCipherEncryption implements EncryptionStrategy {
    private final int shift;

    public CaesarCipherEncryption(int shift) {
        this.shift = shift;
    }

    @Override
    public String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                encryptedText.append((char) ((c - base + shift) % 26 + base));
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }
}

class XOREncryption implements EncryptionStrategy {
    private final String key;

    public XOREncryption(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            encryptedText.append((char) (text.charAt(i) ^ key.charAt(i % key.length())));
        }
        return encryptedText.toString();
    }
}

class TextFileEncryptor {
    private EncryptionStrategy strategy;

    public TextFileEncryptor(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }

    public void encryptFile(File inputFile, File outputFile) {
        try (Scanner scanner = new Scanner(inputFile);
             PrintWriter writer = new PrintWriter(outputFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String encryptedLine = strategy.encrypt(line);
                writer.println(encryptedLine);
            }
            System.out.println("Файл успешно закодирован.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TextFileEncryptor encryptor;

        EncryptionStrategy strategy = new RemoveVowelsEncryption();
        // EncryptionStrategy strategy = new CaesarCipherEncryption(3);
        // EncryptionStrategy strategy = new XOREncryption("key");

        encryptor = new TextFileEncryptor(strategy);
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        encryptor.encryptFile(inputFile, outputFile);
    }
}
