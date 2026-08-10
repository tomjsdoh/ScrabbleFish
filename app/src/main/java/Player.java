import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.List;

public class Player {
    private String name;
    private ArrayList<Tile> rack;
    private int score;

    public Player(String name) {
        this.name = name;
        rack = new ArrayList<>();
        this.score = 0;

    }

    public void drawTiles(TileBag tileBag) {
        for (int i = 0; i < 7; i++) {
            rack.add(tileBag.getRandomTile());
        }
    }

    public ArrayList<Tile> getRack() {
        return rack;
    }

    public void printRack(ArrayList<Tile> rack) {
        for (Tile tile : rack) {
            System.out.print(tile.getLetter() + " ");
        }
    }

    public void printScore() {
        System.out.println(name + "'s score: " + score);
    }

    public void addScore(int addedScore) {
        score = score + addedScore;
    }

    public void playTurn(TileBag tileBag, Board board, Scanner scanner) {
        boolean turnComplete = false;
        System.out.println("It is player " + name + "'s turn.");
        while (!turnComplete) {
            printScore();
            System.out.println("Would you like to play a tile?");
            String answer = scanner.next();
            if (answer == "Y") {
                playTile(tileBag, board, scanner);
            } else if (answer == "N") {
                turnComplete = true;
            } else {
                System.out.println("Invalid answer!");
            }
        }
    }

    public List<Tile> playTile(TileBag tileBag, Board board, Scanner scanner) {
        boolean validTurn = false;

        // create list of played letters
        List<Tile> playedLetters = new ArrayList<>();

        // creates copy of board7
        Board tempBoard = new Board(board);

        // create copy of rack
        ArrayList<Tile> tempRack = (ArrayList<Tile>) rack.clone();

        List<Map<Integer, Integer>> newList = new ArrayList<>();
        while (!validTurn) {
            System.out.println("\n");
            System.out.println(name + "'s rack: ");
            printRack(tempRack);

            // input letter
            System.out.println("What letter would you like to play?: ");
            char letter = scanner.next().charAt(0);

            // input x coordinate
            System.out.println("Please provide the x coordinate for your tile: ");
            int y = scanner.nextInt() - 1;

            // input y coordinate
            System.out.println("Please provide the y coordinate for your tile: ");
            int x = scanner.nextInt() - 1;

            // checks if x and y coordinates are in bounds
            if (tempBoard.isSpaceValid(x, y)) {
                Tile playedLetter = null;

                // searches for letter in player's rack
                for (Tile tile : tempRack) {
                    if (tile.getLetter() == letter) {
                        playedLetter = tile;
                    }
                }

                // if player has the letter they say they do
                if (playedLetter != null) {
                    // remove letter from their rack
                    tempRack.remove(playedLetter);

                    // add letter to playedLetters list
                    playedLetters.add(playedLetter);
                    // tells player if bag is empty.
                    // if it is not player recieves new random tile from tile bag.
                    if (tileBag.isEmpty()) {
                        System.out.println("Bag is empty!");
                    }
                } else {
                    System.out.println("invalid move.");
                }
                // tells player coords are invalid and then repeats loop till correct.
            } else {
                System.out.println("Invalid coordinates!");
            }
        }
    }
}
