package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class BoardsPane extends VBox {

    @FXML
    private VBox parent;
    @FXML
    private Hyperlink defaultBoard;
    @FXML
    private Hyperlink advancedBoard;
    private PopOver popOver;
    private VBox content;


    public BoardsPane() {
        load(this);
        setupContent();
        setupPopOver();
        setupOnMouseEnteredEvents();
    }

    private void setupContent() {
        content = new VBox();
        content.getStyleClass().addAll("board-preview");
    }

    private void setupPopOver() {
        popOver = new PopOver(content);
        popOver.setDetached(false);
        popOver.setDetachable(false);
        popOver.setAutoHide(true);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
        popOver.setHeaderAlwaysVisible(false);
    }

    private void setupOnMouseEnteredEvents() {
        defaultBoard.setOnMouseEntered(event -> {
            content.getStyleClass().remove("advanced");
            content.getStyleClass().add("default");
            popOver.show(defaultBoard);
        });
        advancedBoard.setOnMouseEntered(event -> {
            content.getStyleClass().remove("default");
            content.getStyleClass().add("advanced");
            popOver.show(advancedBoard);
        });
    }
}
