package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.GridPane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class SuspectPane extends GridPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public SuspectPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
