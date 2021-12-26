package agh;

public class FlexMap extends AbstractWorldMap{

    public FlexMap(int x, int y, int ratio) {
        this.ur = new Vector2d(x,y);
        this.ratio=ratio;

        super.jungleSize();
    }

    public boolean isStrict(){return false;}

    @Override
    public boolean canMoveTo(Vector2d position) {
        if( position.x > ur.x) position.x=0;
        if( position.x < 0) position.x=ur.x;
        if( position.y > ur.y) position.y=0;
        if( position.y < 0) position.y=ur.y;
        return true;
    }
}