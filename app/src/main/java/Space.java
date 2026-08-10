public class Space {
    private Tile tile;
    private SpaceType spaceType;

    public enum SpaceType {
        NONE,
        DOUBLE_LETTER,
        DOUBLE_WORD,
        TRIPLE_LETTER,
        TRIPLE_WORD
    }

    public Space() {
        tile = null;
        this.spaceType = SpaceType.NONE;
    }

    public void setType(SpaceType type) {
        spaceType = type;
    }

    public SpaceType getType() {
        return spaceType;
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