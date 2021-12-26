package agh;

import agh.gui.IMapElement;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Grass implements IMapElement {
    private final Vector2d position, jungleLl, jungleUr;

    public Grass(Vector2d position, Vector2d jungleLl, Vector2d jungleUr) {
        this.position = position;
        this.jungleLl=jungleLl;
        this.jungleUr=jungleUr;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public Image getSource() throws FileNotFoundException {
        if( position.follows(jungleLl) && position.precedes((jungleUr)) ) return new Image(new FileInputStream("src/main/resources/grassJungle.png"));
        return new Image(new FileInputStream("src/main/resources/grassSawanna.png"));
    }
}
