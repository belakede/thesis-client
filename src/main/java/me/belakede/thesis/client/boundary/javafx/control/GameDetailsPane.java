package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.BorderPane;
import me.belakede.thesis.client.boundary.javafx.control.controller.GameDetailsPaneController;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.service.SpringFxmlLoader;
import me.belakede.thesis.server.game.domain.Status;

public class GameDetailsPane extends BorderPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public GameDetailsPane(GameSummary gameSummary, Runnable remove, Runnable running) {
        GameDetailsPaneController controller = SPRING_FXML_LOADER.load(this);
        controller.gameIdProperty().bindBidirectional(gameSummary.idProperty());
        controller.boardTypeProperty().bindBidirectional(gameSummary.boardTypeProperty());
        controller.createdProperty().bindBidirectional(gameSummary.createdProperty());
        controller.playersProperty().bindBidirectional(gameSummary.playersProperty());
        controller.statusProperty().bindBidirectional(gameSummary.statusProperty());
        controller.removedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                remove.run();
            }
        });
        controller.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (Status.IN_PROGRESS.equals(newValue)) {
                running.run();
            }
        });
    }

}
