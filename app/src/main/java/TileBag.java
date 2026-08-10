import java.util.ArrayList;
import java.util.Random;

// the pool of unplayed tiles that players draw from
public class TileBag {
    private ArrayList<Tile> tiles;

    public TileBag() {
        tiles = new ArrayList<Tile>();
        initializeTiles();

    }

    // true once every tile has been drawn
    public boolean isEmpty() {
        if (tiles.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    // adds `amount` copies of a letter tile with the given score to the bag
    private void addTiles(char letter, int score, int amount) {
        for (int i = 0; i < amount; i++) {
            tiles.add(new Tile(letter, score));
        }
    }

    // fills the bag with the standard Scrabble tile distribution
    public void initializeTiles() {
        addTiles('A', 1, 9);
        addTiles('B', 3, 2);
        addTiles('C', 3, 2);
        addTiles('D', 2, 4);
        addTiles('E', 1, 12);
        addTiles('F', 4, 2);
        addTiles('G', 2, 3);
        addTiles('H', 4, 2);
        addTiles('I', 1, 9);
        addTiles('J', 8, 1);
        addTiles('K', 5, 1);
        addTiles('L', 1, 4);
        addTiles('M', 3, 2);
        addTiles('N', 1, 6);
        addTiles('O', 1, 8);
        addTiles('P', 3, 2);
        addTiles('Q', 10, 1);
        addTiles('R', 1, 6);
        addTiles('S', 1, 4);
        addTiles('T', 1, 6);
        addTiles('U', 1, 4);
        addTiles('V', 4, 2);
        addTiles('W', 4, 2);
        addTiles('X', 8, 1);
        addTiles('Y', 4, 2);
        addTiles('Z', 10, 1);

        addTiles(' ', 0, 2); // blanks
    }

    // draws and removes a random tile from the bag
    public Tile getRandomTile() {
        Random random = new Random();
        Tile tile = tiles.get(random.nextInt(0, tiles.size()));
        removeTile(tile);
        return tile;

    }

    private void removeTile(Tile tile) {
        tiles.remove(tile);
    }
}
