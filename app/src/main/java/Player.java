import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Player {
    private String name;
    private ArrayList<Tile> rack;
    private int score;

    public Player(String name) {
        this.name = name;
        rack = new ArrayList<Tile>();
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

    public boolean playTurn(TileBag tileBag, Board board, Scanner scanner, boolean firstMove) {
        System.out.println("It is player " + name + "'s turn.");
        printScore();

        while (true) {
            System.out.println("Would you like to play a tile? (Y/N): ");
            String answer = scanner.next();

            if (answer.equalsIgnoreCase("Y")) {
                firstMove = playTile(tileBag, board, scanner, firstMove);
                return firstMove;
            } else if (answer.equalsIgnoreCase("N")) {
                return true;
            }

            System.out.println("Invalid answer!");
        }
    }

    public boolean playTile(TileBag tileBag, Board board, Scanner scanner, boolean firstMove) {
        // tiles are staged on a scratch board/rack until the whole turn is
        // confirmed valid, so a bad word never touches the real game state
        Board tempBoard = new Board(board);

        ArrayList<Tile> tempRack = new ArrayList<>(rack);

        List<Tile> playedLetters = new ArrayList<>();
        List<int[]> playedPositions = new ArrayList<>();

        boolean placingTiles = true;
        while (placingTiles) {
            System.out.println("\n" + name + "'s rack: ");
            printRack(tempRack);

            // input letter
            System.out.println("What letter would you like to play?: ");
            char letter = Character.toUpperCase(scanner.next().charAt(0));

            // input row
            System.out.println("Please provide the row for your tile (1-15): ");
            int row = scanner.nextInt() - 1;

            // input column
            System.out.println("Please provide the column for your tile (1-15): ");
            int col = scanner.nextInt() - 1;

            if (!tempBoard.isSpaceValid(row, col)) {
                System.out.println("Invalid coordinates!");
                continue;
            }

            // searches for letter in player's rack
            Tile playedLetter = null;
            for (Tile tile : tempRack) {
                if (tile.getLetter() == letter) {
                    playedLetter = tile;
                    break;
                }
            }

            if (playedLetter == null) {
                System.out.println("You don't have that letter.");
            }

            tempRack.remove(playedLetter);
            tempBoard.placeTile(row, col, playedLetter);
            playedLetters.add(playedLetter);
            playedPositions.add(new int[] { row, col });

            System.out.println("Place another tile? (Y/N): ");
            if (scanner.next().equalsIgnoreCase("N")) {
                placingTiles = false;
            }
        }

        if (firstMove) {
            boolean coversCenter = false;
            for (int[] pos : playedPositions) {
                if (pos[0] == 7 && pos[1] == 7) {
                    coversCenter = true;
                    break;
                }
            }
            if (!coversCenter) {
                System.out.println("The first move must cover the center square.");
                return true;
            }
        }

        // validate every word touched by this turn's tiles
        Set<WordVals> words = new HashSet<>();
        boolean allValid = true;
        for (int[] pos : playedPositions) {
            int row = pos[0];
            int col = pos[1];

            if (tempBoard.hasHorizontalNeighbour(row, col)) {
                WordVals horizontal = tempBoard.readHorizontalWord(row, col);
                if (horizontal == null) {
                    allValid = false;
                } else {
                    words.add(horizontal);
                }
            }

            if (tempBoard.hasVerticalNeighbour(row, col)) {
                WordVals vertical = tempBoard.readVerticalWord(row, col);
                if (vertical == null) {
                    allValid = false;
                } else {
                    words.add(vertical);
                }
            }
        }

        if (!allValid) {
            System.out.println("That doesn't form valid word(s). Turn cancelled.");
            return true;
        }

        // commit the staged placements to the real board
        for (int i = 0; i < playedPositions.size(); i++) {
            int[] pos = playedPositions.get(i);
            board.placeTile(pos[0], pos[1], playedLetters.get(i));
        }

        // commit the staged rack, then draw replacements for the played tiles
        rack.clear();
        rack.addAll(tempRack);
        for (int i = 0; i < playedLetters.size(); i++) {
            if (tileBag.isEmpty()) {
                System.out.println("Bag is empty!");
                break;
            }
            rack.add(tileBag.getRandomTile());
        }

        int turnScore = 0;
        for (WordVals wordVal : words) {
            turnScore += board.getDictionary().calculateWordValue(wordVal);
        }
        addScore(turnScore);
        System.out.println(name + " scored " + turnScore + " points!");

        return false;
    }

    public boolean isRackEmpty() {
        if (rack.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
