import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

// represents one player: their name, rack, score, and the actions they can take on their turn
public class Player {
    // outcome of a turn: an actual word committed to the board, a voluntary pass,
    // or an attempted play that got rejected (rack/board left untouched)
    public enum TurnResult {
        PLAYED,
        PASSED,
        INVALID
    }

    private String name;
    private ArrayList<Tile> rack;
    private int score;

    public Player(String name) {
        this.name = name;
        rack = new ArrayList<Tile>();
        this.score = 0;

    }

    // fills the rack up to 7 tiles by drawing randomly from the bag
    public void drawTiles(TileBag tileBag) {
        for (int i = 0; i < 7; i++) {
            rack.add(tileBag.getRandomTile());
        }
    }

    public ArrayList<Tile> getRack() {
        return rack;
    }

    // prints every letter currently in the given rack, space separated
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

    // asks the player whether they want to play a tile or pass
    public TurnResult playTurn(TileBag tileBag, Board board, Scanner scanner, boolean firstMove) {
        System.out.println("It is player " + name + "'s turn.");
        printScore();

        while (true) {
            System.out.println("Would you like to play a tile? (Y/N): ");
            String answer = scanner.next();

            if (answer.equalsIgnoreCase("Y")) {
                return playTile(tileBag, board, scanner, firstMove);
            } else if (answer.equalsIgnoreCase("N")) {
                return TurnResult.PASSED;
            }

            System.out.println("Invalid answer!");
        }
    }

    // walks the player through placing tiles for one turn, validates the resulting
    // word(s), and either commits the turn or rejects it and leaves the rack
    // untouched
    public TurnResult playTile(TileBag tileBag, Board board, Scanner scanner, boolean firstMove) {
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
                continue;
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

        // on any turn after the first, at least one placed tile must touch a tile
        // that was already on the board (checked against the real board, not the
        // scratch one, so newly placed tiles don't count as existing neighbors)
        if (firstMove == false) {
            boolean atLeastOneNeighbor = false;
            for (int[] is : playedPositions) {
                int row = is[0];
                int col = is[1];
                if (board.hasHorizontalNeighbour(row, col) || board.hasVerticalNeighbour(row, col)) {
                    atLeastOneNeighbor = true;
                }
            }
            if (atLeastOneNeighbor == false) {
                System.out.println("Your tiles do not have any neighbors. False move!");
                return TurnResult.INVALID;
            }
        }

        // checks tiles played are played in a line
        boolean sameRow = true;
        boolean sameCol = true;
        int r = playedPositions.get(0)[0];
        int c = playedPositions.get(0)[1];
        for (int[] is : playedPositions) {
            if (is[0] != r)
                sameRow = false;
            if (is[1] != c)
                sameCol = false;
        }

        if (!sameRow && !sameCol) {
            System.out.println("Tiles not played in a line!");
            return TurnResult.INVALID;
        }

        // the very first move of the game must cover the center square
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
                return TurnResult.INVALID;
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
            return TurnResult.INVALID;
        }

        // checks if no neighbors and not first turn
        if (words.isEmpty() && firstMove == false) {
            System.out.println("Word played has no neighbors!");
            return TurnResult.INVALID;
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

        // checks bingo bonus requirement (+50 for 7 tiles played at once)
        if (playedLetters.size() == 7) {
            turnScore += 50;
        }

        // adds score to player
        addScore(turnScore);

        // prints score for round
        System.out.println(name + " scored " + turnScore + " points!");

        return TurnResult.PLAYED;
    }

    // true once the player has no tiles left, used for the end-game check
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
