package agh;

public class StrictMap extends AbstractWorldMap {

    public StrictMap(int x, int y, int ratio){
        this.ur = new Vector2d(x,y);
        this.ratio=ratio;

        super.jungleSize();
    }
    public boolean isStrict(){return true;}

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(ur) && position.follows(ll);
    }
}