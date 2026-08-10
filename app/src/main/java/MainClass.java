public class MainClass {
    public static void main(String[] args) {
        Board board = new Board(15);
        TileBag tileBag = new TileBag();
        board.insertTile(11, 12, tileBag.getRandomTile());
        board.printBoard();
    }
}
