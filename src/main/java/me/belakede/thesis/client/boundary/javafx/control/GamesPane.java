package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.VBox;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class GamesPane extends VBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public GamesPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
