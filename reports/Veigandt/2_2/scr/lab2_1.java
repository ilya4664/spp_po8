import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String filePath = "C:\\Work\\Labs\\3 course 2 sem\\SPP\\lab2\\lab2_1\\src\\123.txt";
        Map<Integer, Integer> wordLengthFrequency = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    word = word.replaceAll("[^a-zA-Z]", "");
                    int length = word.length();
                    if (length > 0) {
                        int count = wordLengthFrequency.getOrDefault(length, 0);
                        wordLengthFrequency.put(length, count + 1);
                    }
                }
            }

            System.out.println("Length Frequency");
            for (Map.Entry<Integer, Integer> entry : wordLengthFrequency.entrySet()) {
                System.out.println(entry.getKey() + "\t\t" + entry.getValue());
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
