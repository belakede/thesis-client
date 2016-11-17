package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class HistoryPane extends StackPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public HistoryPane() {
        SPRING_FXML_LOADER.load(this);
    }

}
