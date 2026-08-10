public class Board {
    private Space[][] board;

    public Board(int size) {
        // initialises board with empty spaces.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Space();
            }
        }
    }
}