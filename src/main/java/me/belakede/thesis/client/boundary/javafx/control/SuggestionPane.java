package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.GridPane;
import me.belakede.thesis.client.boundary.javafx.control.controller.SuggestionPaneController;
import me.belakede.thesis.client.boundary.javafx.model.SuggestionType;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class SuggestionPane extends GridPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public SuggestionPane(SuggestionType type) {
        SuggestionPaneController controller = SPRING_FXML_LOADER.load(this);
        controller.setType(type);
    }

}
