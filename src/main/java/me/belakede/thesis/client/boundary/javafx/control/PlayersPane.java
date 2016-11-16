package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.VBox;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class PlayersPane extends VBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public PlayersPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
