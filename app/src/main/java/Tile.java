// a single letter tile with its point value (0 for a blank)
public class Tile {
    private char letter;
    private int value;
    private char altLetter;

    public Tile(char letter, int value) {
        this.letter = letter;
        this.value = value;
    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }

    public void setAltLetter(char alt) {
        altLetter = alt;
    }

    public char getAltLetter() {
        return altLetter;
    }
}