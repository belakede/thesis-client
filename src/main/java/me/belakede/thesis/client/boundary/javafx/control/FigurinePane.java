package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.control.controller.FigurinePaneController;
import me.belakede.thesis.client.service.SpringFxmlLoader;
import me.belakede.thesis.game.equipment.Figurine;

public class FigurinePane extends VBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public FigurinePane(Figurine figurine) {
        FigurinePaneController controller = SPRING_FXML_LOADER.load(this);
        controller.setFigurine(figurine);
    }

}
