package agh;

enum MapDirection {
    NORTH_WEST,
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST;

    public MapDirection random() {
        int q = (int) (Math.random()*8);
        return switch ( q )  {
            case 0->NORTH;
            case 1->SOUTH;
            case 2->EAST;
            case 3->WEST;
            case 4->NORTH_WEST;
            case 5->SOUTH_EAST;
            case 6->NORTH_EAST;
            default->SOUTH_WEST;
        };
    }
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
            case SOUTH_EAST -> "SE";
            case NORTH_EAST -> "NE";
            case SOUTH_WEST -> "SW";
        };
    }
    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0,1);
            case NORTH_WEST -> new Vector2d(-1,1);
            case NORTH_EAST -> new Vector2d(1,1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case EAST -> new Vector2d(1,0);
            case WEST -> new Vector2d(-1,0);
        };
    }
}
