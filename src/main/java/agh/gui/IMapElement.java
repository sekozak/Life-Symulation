package agh.gui;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;

public interface IMapElement {
    Image getSource() throws FileNotFoundException;
}
