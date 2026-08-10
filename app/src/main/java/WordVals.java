
import java.util.HashSet;

// a validated word: its text plus the set of board spaces it occupies (used for
// scoring and for de-duplicating a word read from more than one placed tile)
public class WordVals {
    private String wordString;
    private HashSet<Space> wordSpaces;

    public WordVals() {
    }

    public void setWordSpaces(HashSet<Space> wordSpaces) {
        this.wordSpaces = wordSpaces;
    }

    public void setWordString(String wordString) {
        this.wordString = wordString;
    }

    public String getWordString() {
        return wordString;
    }

    public HashSet<Space> getWordSpaces() {
        return wordSpaces;
    }

    // two WordVals are the same word if they occupy the same set of spaces, so the
    // same word read from two different placed tiles collapses to one entry in a Set
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WordVals)) {
            return false;
        }
        return wordSpaces.equals(((WordVals) obj).wordSpaces);
    }

    @Override
    public int hashCode() {
        return wordSpaces.hashCode();
    }

}
