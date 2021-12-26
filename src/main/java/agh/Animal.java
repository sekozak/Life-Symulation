package agh;

import agh.gui.IMapElement;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement {
    private Vector2d position;
    private MapDirection orientation=MapDirection.NORTH;
    private final IWorldMap map;
    private List<IPositionChangeObserver> observers = new ArrayList<>();
    private Genes gen;
    private int energy,startEnergy;


    public Animal(IWorldMap map, Vector2d initialPosition, int startEnergy) {
        this.position = initialPosition;
        this.orientation = orientation.random();
        this.map = map;
        this.energy = startEnergy;
        this.startEnergy = startEnergy;
        this.gen = new Genes();
    }


    public void eat(int value){
        this.energy += value;
    }

    public boolean isDead() {
        return this.energy <= 0;
    }


    public Animal sex(Animal parent){
        int energyMoher = this.energy;
        int energyFather = parent.energy;
        int p = energyFather/(energyFather+energyMoher)*32;
        int childenergy = (int) (this.energy*0.25 + parent.energy*0.25);
        this.energy *= 0.75;
        parent.energy *= 0.75;

        Genes childGen = new Genes( this.gen, parent.gen, p );
        Animal child = new Animal(this.map, this.position, childenergy);
        child.gen = childGen;

        return child;
    }

    public void move(Integer direction, int moveEnergy){
        this.energy-=moveEnergy;
        switch (direction) {
            case 0 -> {
                Vector2d p = position.add(orientation.toUnitVector());
                if( map.canMoveTo(p) ) {
                    Vector2d oldposition=position;
                    position = p;
                    positionChanged(oldposition,position);
                }
            }
            case 4 -> {
                Vector2d p = position.subtract(orientation.toUnitVector());
                if( map.canMoveTo(p) ){
                    Vector2d oldposition=position;
                    position=p;
                    positionChanged(oldposition,position);
                }
            }
            default -> {
                for ( int i=0; i<direction; i++) {
                    orientation = orientation.next();
                }
            }
        }
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver q: observers){
            q.positionChanged(oldPosition,newPosition,this);
        }
    }


    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() { return energy; }

    public void setGen(Genes gen) {
        this.gen = gen;
    }
    public Genes getGen() {
        return gen;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    @Override
    public Image getSource() throws FileNotFoundException {
        if(energy>startEnergy) {
            return new Image(new FileInputStream("src/main/resources/" +
                    switch (this.orientation) {
                        case NORTH -> "up1.png";
                        case NORTH_EAST -> "upRight1.png";
                        case NORTH_WEST -> "upLeft1.png";
                        case SOUTH -> "down1.png";
                        case SOUTH_EAST -> "downRight1.png";
                        case SOUTH_WEST -> "downLeft1.png";
                        case EAST -> "right1.png";
                        case WEST -> "left1.png";
                    }));
        }
        else if(energy>0.5*startEnergy) {
            return new Image(new FileInputStream("src/main/resources/" +
                    switch (this.orientation) {
                        case NORTH -> "up2.png";
                        case NORTH_EAST -> "upRight2.png";
                        case NORTH_WEST -> "upLeft2.png";
                        case SOUTH -> "down2.png";
                        case SOUTH_EAST -> "downRight2.png";
                        case SOUTH_WEST -> "downLeft2.png";
                        case EAST -> "right2.png";
                        case WEST -> "left2.png";
                    }));
        }
        return new Image(new FileInputStream("src/main/resources/" +
                switch (this.orientation) {
                    case NORTH -> "up3.png";
                    case NORTH_EAST -> "upRight3.png";
                    case NORTH_WEST -> "upLeft3.png";
                    case SOUTH -> "down3.png";
                    case SOUTH_EAST -> "downRight3.png";
                    case SOUTH_WEST -> "downLeft3.png";
                    case EAST -> "right3.png";
                    case WEST -> "left3.png";
                }));
    }
}

