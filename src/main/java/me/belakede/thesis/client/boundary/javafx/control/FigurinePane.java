package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.controller.FigurinePaneController;
import me.belakede.thesis.client.service.SpringFxmlLoader;
import me.belakede.thesis.game.equipment.Figurine;

public class FigurinePane extends StackPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();
    private FigurinePaneController controller;

    public FigurinePane() {
        controller = SPRING_FXML_LOADER.load(this);
    }

    public void setFigurine(Figurine figurine) {
        controller.setFigurine(figurine);
    }

}
