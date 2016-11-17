package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.HBox;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class CardPane extends HBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public CardPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
