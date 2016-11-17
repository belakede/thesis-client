package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.BorderPane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class GamePane extends BorderPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public GamePane() {
        SPRING_FXML_LOADER.load(this);
    }

}
