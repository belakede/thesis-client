package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.controller.FieldPaneController;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class FieldPane extends StackPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public FieldPane(int row, int column) {
        FieldPaneController controller = SPRING_FXML_LOADER.load(this);
        controller.setField(row, column);
    }

}
