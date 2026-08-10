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
            return (board[i][j].getTileLetter());
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
        if (board[x][y].getTile() == null) {
            board[x][y].setTile(tile);
        } else {
            System.out.println("This space is already taken.");
        }
    }
}