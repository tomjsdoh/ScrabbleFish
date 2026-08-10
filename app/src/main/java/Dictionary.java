import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// holds the valid-word list and per-letter point values used for scoring
public class Dictionary {
    private static final String FILE_NAME = "dictionary.txt";

    private Set<String> dictionary = new HashSet<>();
    private Map<Character, Integer> letterValues = new HashMap<>();

    public Dictionary() {
        initialiseLetterValues();
        saveWords();
    }

    // loads the word list from the bundled dictionary.txt resource
    private void saveWords() {
        try (InputStream stream = Dictionary.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (stream == null) {
                System.out.println("dictionary resource not found: " + FILE_NAME);
                return;
            }

            // takes each word from text dictionary and calculates its value
            try (Scanner scanner = new Scanner(stream)) {
                while (scanner.hasNextLine()) {
                    String word = scanner.nextLine().trim().toUpperCase();
                    // pairs word and value and adds to dictionary hashmap
                    dictionary.add(word);
                }
            }
        } catch (IOException e) {
            System.out.println("failed to read dictionary resource: " + FILE_NAME);
        }
    }

    // scores a word: letter values (with letter multipliers) summed, then scaled
    // by any word multipliers among the spaces it occupies
    public int calculateWordValue(WordVals word) {
        if (word == null) {
            return 0;
        }

        int letterScore = 0;
        int wordMultiplier = 1;

        // sums letter values (applying letter multipliers) and accumulates
        // any word multipliers from the spaces the word occupies
        for (Space space : word.getWordSpaces()) {
            int letterValue = letterValues.get(space.getSpaceLetter());

            switch (space.getType()) {
                case Space.SpaceType.DOUBLE_LETTER:
                    letterValue *= 2;
                    break;
                case Space.SpaceType.TRIPLE_LETTER:
                    letterValue *= 3;
                    break;
                case Space.SpaceType.DOUBLE_WORD:
                    wordMultiplier *= 2;
                    break;
                case Space.SpaceType.TRIPLE_WORD:
                    wordMultiplier *= 3;
                    break;
                default:
                    break;
            }

            letterScore += letterValue;
        }

        return letterScore * wordMultiplier;
    }

    public boolean isWord(String word) {
        return dictionary.contains(word);
    }

    // standard Scrabble letter point values
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
