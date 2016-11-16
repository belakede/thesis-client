package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class NotePane extends StackPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public NotePane() {
        SPRING_FXML_LOADER.load(this);
    }

}
