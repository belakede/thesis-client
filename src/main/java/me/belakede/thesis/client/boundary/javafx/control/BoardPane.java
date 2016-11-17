package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.TilePane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class BoardPane extends TilePane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public BoardPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
