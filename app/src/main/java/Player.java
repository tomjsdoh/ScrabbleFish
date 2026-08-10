import java.util.ArrayList;
import java.util.Scanner;

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

    public void printRack() {
        for (Tile tile : rack) {
            System.out.print(tile.getLetter() + " ");
        }
    }

    public void addScore(int addedScore) {
        score = score + addedScore;
    }

    public void playTile(TileBag tileBag, Board board, Scanner scanner) {
        boolean validTurn = false;

        while (!validTurn) {
            System.out.println("\n");
            System.out.println(name + "'s rack: ");
            printRack();
            System.out.println("\n" + name + "'s score: " + score);

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
            if (board.isSpaceValid(x, y)) {
                Tile playedLetter = null;

                // searches for letter in player's rack
                for (Tile tile : rack) {
                    if (tile.getLetter() == letter) {
                        playedLetter = tile;
                    }
                }

                // if player has the letter they say they do
                if (playedLetter != null) {
                    // remove letter from their rack
                    rack.remove(playedLetter);

                    try {
                        board.insertTile(x, y, playedLetter);
                        validTurn = true;
                    } catch (Board.InvalidWord e) {
                        rack.add(playedLetter);
                        System.out.println(e.getMessage());
                    }

                    // adds scores of horizontal and vertical words together
                    String horizontalWord = board.readHorizontalWord(x, y);
                    String verticalWord = board.readVerticalWord(x, y);
                    Dictionary dictionary = new Dictionary();
                    int addedScore = dictionary.calculateWordValue(horizontalWord)
                            + dictionary.calculateWordValue(verticalWord);

                    // adds word scores to total
                    score += addedScore;

                    // tells player if bag is empty.
                    // if it is not player recieves new random tile from tile bag.
                    if (tileBag.isEmpty()) {
                        System.out.println("Bag is empty!");
                    } else {
                        rack.add(tileBag.getRandomTile());
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
