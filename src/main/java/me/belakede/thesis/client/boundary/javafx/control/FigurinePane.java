package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class FigurinePane extends StackPane {

    @FXML
    private Circle content;

    public FigurinePane() {
        load();
    }

    private void load() {
        ControlLoader.load(this);
    }

}
