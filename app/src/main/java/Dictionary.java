import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Dictionary {
    private Set<String> dictionary = new HashSet<>();
    private Map<Character, Integer> letterValues = new HashMap<>();
    private final File file;
    private String fileName;

    public Dictionary() {
        fileName = "dictionary.txt";
        file = new File(fileName);
        initialiseLetterValues();
        saveWords();
    }

    private void saveWords() {
        try {
            // init scanner
            Scanner scanner = new Scanner(file);

            // takes each word from text dictionary and calculates its value
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toUpperCase();
                // pairs word and value and adds to dictionary hashmap
                dictionary.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found: " + file);
        }
    }

    public int calculateWordValue(String word) {
        int wordScore = 0;

        // calculates value for each letter then totals the words score
        for (int i = 0; i < word.length(); i++) {
            // uses values from letterValues
            wordScore += letterValues.get(word.charAt(i));
        }
        return wordScore;
    }

    public boolean isWord(String word) {
        // if word score is NOT null, it must exist in the dictionary
        return dictionary.contains(word);
    }

    private void initialiseLetterValues() {
        // initialises letters and their corresponding values.
        letterValues.put('A', 1);
        letterValues.put('B', 3);
        letterValues.put('C', 3);
        letterValues.put('D', 2);
        letterValues.put('E', 1);
        letterValues.put('F', 4);
        letterValues.put('G', 2);
        letterValues.put('H', 4);
        letterValues.put('I', 1);
        letterValues.put('J', 8);
        letterValues.put('K', 5);
        letterValues.put('L', 1);
        letterValues.put('M', 3);
        letterValues.put('N', 1);
        letterValues.put('O', 1);
        letterValues.put('P', 3);
        letterValues.put('Q', 10);
        letterValues.put('R', 1);
        letterValues.put('S', 1);
        letterValues.put('T', 1);
        letterValues.put('U', 1);
        letterValues.put('V', 4);
        letterValues.put('W', 4);
        letterValues.put('X', 8);
        letterValues.put('Y', 4);
        letterValues.put('Z', 10);
        letterValues.put(' ', 0); // Blank tile
    }
}
