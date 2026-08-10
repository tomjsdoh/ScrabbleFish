public class Board {
    private Space[][] board;
    private int size;

    public Board(int size) {
        // initialises board with empty spaces.
        this.size = size;
        board = new Space[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Space();
            }
        }
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
        if (readHorizontalWord(x, y) != null && readVerticalWord(x, y) != null) {
            board[x][y].setTile(tile);
        } else {
            throw new InvalidWord("Words constructed are not valid.");
        }
    }

    // custom error for invalid word
    public class InvalidWord extends RuntimeException {
        public InvalidWord(String message) {
            super(message);
        }
    }

    public boolean isSpaceValid(int x, int y) {
        if (x > 0 && x < 15 && y > 0 && y < 15 && board[x][y].getTile() == null) {
            return true;
        } else {
            return false;
        }

    }

    public String readHorizontalWord(int row, int col) {
        Dictionary dictionary = new Dictionary();

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
        for (int i = start; i <= col; i++) {
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
        Dictionary dictionary = new Dictionary();

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
        for (int i = start; i <= col; i++) {
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
}