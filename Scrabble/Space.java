public class Space{
    private Tile tile;
    private boolean isEmpty;
    private int multiplier;


    public Space(){
        this.tile = null;
        this.isEmpty = true;
    }
    public void setTile(Tile tile){
        this.tile = tile;
        this.isEmpty = false;
    }
    public void setMultiplier(int multiplier){
        this.multiplier = multiplier;
    }
    public Tile getTile(){
        return tile;
    }
    public boolean isEmpty(){
        return isEmpty;
    }
    public int getMultiplier(){
        return multiplier;
    }
}