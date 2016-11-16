package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.BorderPane;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class ChatBox extends BorderPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public ChatBox() {
        SPRING_FXML_LOADER.load(this);
    }

}
