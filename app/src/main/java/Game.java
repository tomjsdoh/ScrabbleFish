import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean gameFinished = false;
        // initialise board
        Board board = new Board(15);

        // init tilebag
        TileBag tileBag = new TileBag();

        // init player 1
        System.out.println("Enter Player 1's name: ");
        Player player1 = new Player(scanner.nextLine());
        player1.drawTiles(tileBag);

        // init player 2
        System.out.println("Enter Player 2's name: ");
        Player player2 = new Player(scanner.nextLine());
        player2.drawTiles(tileBag);

        // game loops
        while (!gameFinished) {
            board.printBoard();
            player1.playTile(tileBag, board, scanner);

            board.printBoard();
            player2.playTile(tileBag, board, scanner);
        }

    }

}
