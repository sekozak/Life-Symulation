package agh.gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private VBox vBox;
    private Image image;
    public GuiElementBox(IMapElement object) {
        try {
            image = object.getSource();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public VBox elementVisualization(){
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        vBox = new VBox(imageView);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

}
