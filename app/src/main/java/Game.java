import java.util.Scanner;

// entry point: sets up a two-player game and runs the turn loop until it ends
public class Game {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

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
        boolean gameFinished = false;
        // game loops, checking for game-over right after each individual turn
        while (!gameFinished) {

            // player 1 turn
            board.printBoard();
            Player.TurnResult result1 = player1.playTurn(tileBag, board, scanner, firstMove);
            if (firstMove && result1 == Player.TurnResult.PLAYED) {
                firstMove = false;
            }
            passCount = updatePassCount(passCount, result1);
            if (isGameOver(player1, player2, tileBag, passCount)) {
                gameFinished = true;
                break;
            }

            // player 2 turn
            board.printBoard();
            Player.TurnResult result2 = player2.playTurn(tileBag, board, scanner, firstMove);
            if (firstMove && result2 == Player.TurnResult.PLAYED) {
                firstMove = false;
            }
            passCount = updatePassCount(passCount, result2);
            if (isGameOver(player1, player2, tileBag, passCount)) {
                gameFinished = true;
                break;
            }
        }

        int p1FinalScore = calculateFinalScore(player1, player2, tileBag);
        int p2FinalScore = calculateFinalScore(player2, player1, tileBag);
        // whoever has the higher score wins; equal scores are a draw
        if (p1FinalScore == p2FinalScore) {
            System.out.println("AAAAAAAND it's a draw... :(");
        } else {
            Player winner = null;
            if (p1FinalScore > p2FinalScore) {
                winner = player1;
            } else {
                winner = player2;
            }
            System.out.println("THE WINNER IS....");
            System.out.println(winner.getName().toUpperCase() + "!");
        }

    }

    // updates passcount correctly dependent on action taken during turn
    public static int updatePassCount(int passCount, Player.TurnResult result) {
        if (result == Player.TurnResult.PASSED) {
            return passCount + 1;
        } else if (result == Player.TurnResult.PLAYED) {
            return 0;
        } else {
            return passCount;
        }
    }

    // game ends when either player has emptied their rack with an empty bag left to
    // draw from, or after 4 consecutive passes
    public static boolean isGameOver(Player player1, Player player2, TileBag tileBag, int passCount) {
        if ((player1.isRackEmpty() || player2.isRackEmpty()) && tileBag.isEmpty()) {
            return true;
        } else if (passCount >= 4) {
            return true;
        } else {
            return false;
        }
    }

    // sums the point value of every tile left in a player's rack
    public static int rackValue(Player player) {
        int total = 0;
        for (Tile tile : player.getRack()) {
            total += tile.getValue();
        }
        return total;
    }

    // every player loses the value of their own unplayed tiles; if this player is
    // the one who emptied their rack (ending the game), they also gain the value
    // of the opponent's unplayed tiles
    public static int calculateFinalScore(Player player, Player opponent, TileBag tileBag) {
        int finalScore = player.getScore() - rackValue(player);
        if (player.isRackEmpty() && tileBag.isEmpty()) {
            finalScore += rackValue(opponent);
        }
        return finalScore;
    }

}
