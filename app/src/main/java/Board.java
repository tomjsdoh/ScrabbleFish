import Space.spaceTypes;

public class Board {
    private Space[][] board;
    private int size;
    private Dictionary dictionary;

    public Board(int size, Dictionary dictionary) {
        // initialises board with empty spaces.
        this.size = size;
        board = new Space[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Space();
            }
        }
        this.dictionary = dictionary;
    }

    // initialise special tiles
    public Board initialiseSpecialTiles(Board board) {

    }

    public char printSpace(int i, int j) {
        if (board[i][j].getTile() == null) {
            return '#';
        } else {
            return (board[i][j].getSpaceLetter());
        }

    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(printSpace(i, j) + " ");
            }
            System.out.println();
        }
    }

    public void insertTile(int x, int y, Tile tile) {
        board[x][y].setTile(tile);

        if (hasHorizontalNeighbour(x, y) && readHorizontalWord(x, y) == null) {
            board[x][y].setTile(null);
            throw new InvalidWord("Invalid horizontal word.");
        }

        if (hasVerticalNeighbour(x, y) && readVerticalWord(x, y) == null) {
            board[x][y].setTile(null);
            throw new InvalidWord("Invalid vertical word.");
        }
    }

    // custom error for invalid word
    public final class InvalidWord extends RuntimeException {
        public InvalidWord(String message) {
            super(message);
        }
    }

    public boolean isSpaceValid(int x, int y) {
        if (x >= 0 && x < 15 && y >= 0 && y < 15 && board[x][y].getTile() == null) {
            return true;
        } else {
            return false;
        }

    }

    public String readHorizontalWord(int row, int col) {

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
            stringBuilder.append(board[row][i].getSpaceLetter());
        }

        // stores word
        String word = stringBuilder.toString();

        // returns word if word is valid, otherwise null
        if (dictionary.isWord(word) == true) {
            return word;
        } else {
            return null;
        }
    }

    public String readVerticalWord(int row, int col) {

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
            stringBuilder.append(board[i][col].getSpaceLetter());
        }

        // stores word
        String word = stringBuilder.toString();

        // returns word if word is valid, otherwise null
        if (dictionary.isWord(word) == true) {
            return word;
        } else {
            return null;
        }
    }

    private boolean hasHorizontalNeighbour(int row, int col) {
        return (col > 0 && board[row][col - 1].getTile() != null) ||
                (col < size - 1 && board[row][col + 1].getTile() != null);
    }

    private boolean hasVerticalNeighbour(int row, int col) {
        return (row > 0 && board[row - 1][col].getTile() != null) ||
                (row < size - 1 && board[row + 1][col].getTile() != null);
    }
}