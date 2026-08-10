import java.util.Scanner;

public class Game {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean gameFinished = false;

        // init dictionary
        Dictionary dictionary = new Dictionary();
        // initialise board
        Board board = new Board(15, dictionary);

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
        boolean firstMove = true;
        int passCount = 0;
        // game loops
        while (!gameFinished) {

            // player 1 turn
            board.printBoard();
            boolean passed1 = player1.playTurn(tileBag, board, scanner, firstMove);
            // checks if player 1 played turn
            if (firstMove && !passed1) {
                firstMove = false;
            }

            // player 2 turn
            board.printBoard();
            boolean passed2 = player2.playTurn(tileBag, board, scanner, firstMove);
            if (firstMove && !passed2) {
                firstMove = false;
            }

            if (passed1 && passed2) {
                passCount += 2;
            } else {
                passCount = 0;
            }

            if ((player1.isRackEmpty() || player2.isRackEmpty()) && tileBag.isEmpty()) {
                gameFinished = true;
            } else if (passCount >= 4) {
                gameFinished = true;
            }
        }

        if (player1.getScore() == player2.getScore()) {
            System.out.println("AAAAAAAND it's a draw... :(");
        } else {
            Player winner = null;
            if (player1.getScore() > player2.getScore()) {
                winner = player1;
            } else {
                winner = player2;
            }
            System.out.println("THE WINNER IS....");
            System.out.println(winner.getName().toUpperCase() + "!");
        }

    }

}
