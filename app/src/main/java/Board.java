import java.util.HashSet;
import java.util.List;

// the 15x15 grid of spaces, plus the word-reading and validity checks that operate on it
public class Board {
    private Space[][] board;
    private int size;
    private Dictionary dictionary;

    public Board(Dictionary dictionary) {
        // initialises board with empty spaces.
        size = 15;
        board = new Space[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Space();
            }
        }
        initialiseSpecialTiles();
        this.dictionary = dictionary;
    }

    // deep copy used to stage a turn's placements without touching the real board
    public Board(Board other) {
        size = other.size;
        dictionary = other.dictionary;

        board = new Space[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Space otherSpace = other.board[row][col];
                board[row][col] = new Space(otherSpace.getType());
                board[row][col].setTile(otherSpace.getTile());
            }
        }
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    // places a tile without validating the word it forms; used while a turn is
    // still being assembled and intermediate placements aren't yet complete words
    public void placeTile(int row, int col, Tile tile) {
        board[row][col].setTile(tile);
    }

    // initialise special tiles
    public void initialiseSpecialTiles() {
        int[][] tripleWords = {
                { 0, 0 },
                { 0, 7 },
                { 0, 14 },

                { 7, 0 },
                { 7, 14 },

                { 14, 0 },
                { 14, 7 },
                { 14, 14 }
        };

        int[][] doubleWords = {
                { 1, 1 },
                { 2, 2 },
                { 3, 3 },
                { 4, 4 },

                { 7, 7 },

                { 10, 10 },
                { 11, 11 },
                { 12, 12 },
                { 13, 13 },

                { 1, 13 },
                { 2, 12 },
                { 3, 11 },
                { 4, 10 },

                { 10, 4 },
                { 11, 3 },
                { 12, 2 },
                { 13, 1 }
        };

        int[][] tripleLetters = {
                { 1, 5 },
                { 1, 9 },

                { 5, 1 },
                { 5, 5 },
                { 5, 9 },
                { 5, 13 },

                { 9, 1 },
                { 9, 5 },
                { 9, 9 },
                { 9, 13 },

                { 13, 5 },
                { 13, 9 }
        };

        int[][] doubleLetters = {
                { 0, 3 },
                { 0, 11 },

                { 2, 6 },
                { 2, 8 },

                { 3, 0 },
                { 3, 7 },
                { 3, 14 },

                { 6, 2 },
                { 6, 6 },
                { 6, 8 },
                { 6, 12 },

                { 7, 3 },
                { 7, 11 },

                { 8, 2 },
                { 8, 6 },
                { 8, 8 },
                { 8, 12 },

                { 11, 0 },
                { 11, 7 },
                { 11, 14 },

                { 12, 6 },
                { 12, 8 },

                { 14, 3 },
                { 14, 11 }
        };

        for (int[] pos : tripleWords) {
            board[pos[0]][pos[1]].setType(Space.SpaceType.TRIPLE_WORD);
        }

        for (int[] pos : doubleWords) {
            board[pos[0]][pos[1]].setType(Space.SpaceType.DOUBLE_WORD);
        }

        for (int[] pos : tripleLetters) {
            board[pos[0]][pos[1]].setType(Space.SpaceType.TRIPLE_LETTER);
        }

        for (int[] pos : doubleLetters) {
            board[pos[0]][pos[1]].setType(Space.SpaceType.DOUBLE_LETTER);
        }
    }

    public char printSpace(int i, int j) {
        Space space = board[i][j];

        // checks if space is free
        if (space.getTile() == null) {
            switch (space.getType()) {
                case Space.SpaceType.TRIPLE_WORD:
                    return '!';
                case Space.SpaceType.DOUBLE_WORD:
                    return '?';
                case Space.SpaceType.TRIPLE_LETTER:
                    return '3';
                case Space.SpaceType.DOUBLE_LETTER:
                    return '2';
                default:
                    return '#';
            }
        } else {
            char letter = space.getSpaceLetter();
            // a placed blank displays the letter it was assigned, not '~'
            if (letter == '~') {
                letter = space.getTile().getAltLetter();
            }
            return letter;
        }

    }

    // prints the whole grid, row by row, using printSpace for each cell
    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(printSpace(i, j) + " ");
            }
            System.out.println();
        }
    }

    // custom error for invalid word
    public final class InvalidWord extends RuntimeException {
        public InvalidWord(String message) {
            super(message);
        }
    }

    // a space is valid to place on if it's on the board and not already occupied
    public boolean isSpaceValid(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size && board[x][y].getTile() == null) {
            return true;
        } else {
            return false;
        }

    }

    public WordVals readHorizontalWord(int row, int col) {
        HashSet<Space> wordSpaces = new HashSet<>();
        // init start and end of string
        int start = col;
        int end = col;
        StringBuilder stringBuilder = new StringBuilder();

        // finds start of word
        while (start > 0 && board[row][start - 1].getTile() != null) {
            start--;
        }

        // finds end of word
        while (end < size - 1 && board[row][end + 1].getTile() != null) {
            end++;
        }

        // builds word from start to end point
        for (int i = start; i <= end; i++) {
            char letter = board[row][i].getSpaceLetter();
            Tile tile = board[row][i].getTile();
            if (letter == '~') {
                letter = tile.getAltLetter();
            }
            stringBuilder.append(letter);
            wordSpaces.add(board[row][i]);
        }

        // stores word
        String word = stringBuilder.toString();

        // returns word if word is valid, otherwise null
        if (dictionary.isWord(word) == true) {
            WordVals wordVals = new WordVals();
            wordVals.setWordSpaces(wordSpaces);
            wordVals.setWordString(word);
            return wordVals;
        } else {
            return null;
        }
    }

    public WordVals readVerticalWord(int row, int col) {
        HashSet<Space> wordSpaces = new HashSet<>();
        // init start and end of string
        int start = row;
        int end = row;
        StringBuilder stringBuilder = new StringBuilder();

        // finds start of word
        while (start > 0 && board[start - 1][col].getTile() != null) {
            start--;
        }

        // finds end of word
        while (end < size - 1 && board[end + 1][col].getTile() != null) {
            end++;
        }

        // builds word from start to end point
        for (int i = start; i <= end; i++) {
            char letter = board[i][col].getSpaceLetter();
            Tile tile = board[i][col].getTile();
            if (letter == '~') {
                letter = tile.getAltLetter();
            }
            stringBuilder.append(letter);
            wordSpaces.add(board[i][col]);
        }

        // stores word
        String word = stringBuilder.toString();

        // returns word if word is valid, otherwise null
        if (dictionary.isWord(word) == true) {
            WordVals wordVals = new WordVals();
            wordVals.setWordSpaces(wordSpaces);
            wordVals.setWordString(word);
            return wordVals;
        } else {
            return null;
        }
    }

    // true if the space immediately left or right is occupied
    public boolean hasHorizontalNeighbour(int row, int col) {
        return (col > 0 && board[row][col - 1].getTile() != null) ||
                (col < size - 1 && board[row][col + 1].getTile() != null);
    }

    // true if the space immediately above or below is occupied
    public boolean hasVerticalNeighbour(int row, int col) {
        return (row > 0 && board[row - 1][col].getTile() != null) ||
                (row < size - 1 && board[row + 1][col].getTile() != null);
    }
}