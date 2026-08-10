
import java.util.HashSet;

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
