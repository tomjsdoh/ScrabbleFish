
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
        initialiseSpecialTiles();
        this.dictionary = dictionary;
    }

    public Board(Board other) {
        size = other.size;
        dictionary = other.dictionary;

        board = new Space[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = new Space(other.board[row][col].getType());
            }
        }
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