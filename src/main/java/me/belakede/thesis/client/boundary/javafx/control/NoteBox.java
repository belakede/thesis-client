package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.GridPane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class NoteBox extends GridPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public NoteBox() {
        SPRING_FXML_LOADER.load(this);
    }

}
