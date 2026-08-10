public class Space {
    private Tile tile;
    private spaceTypes spaceType;

    public enum spaceTypes {
        NONE,
        DOUBLE_LETTER,
        DOUBLE_WORD,
        TRIPLE_LETTER,
        TRIPPLE_WORD
    }

    public Space(spaceTypes spaceType) {
        tile = null;
        this.spaceType = spaceType;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile newTile) {
        tile = newTile;
    }

    public char getSpaceLetter() {
        if (tile == null) {
            return '#';
        }

        return tile.getLetter();
    }

}