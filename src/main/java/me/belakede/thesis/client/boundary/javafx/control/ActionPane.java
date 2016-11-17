package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.VBox;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class ActionPane extends VBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public ActionPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
