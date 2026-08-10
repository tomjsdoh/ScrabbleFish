public class Space {
    private Tile tile;

    public Space() {
        tile = null;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile newTile) {
        tile = newTile;
    }

    public char getSpaceLetter() {
        return tile.getLetter();
    }

}