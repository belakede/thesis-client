package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BoardsPaneController implements Initializable {

    @FXML
    private Hyperlink defaultBoard;
    @FXML
    private Hyperlink advancedBoard;
    private PopOver popOver;
    private VBox content;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
        popOver.setHeaderAlwaysVisible(false);
    }

    private void setupOnMouseEnteredEvents() {
        defaultBoard.setOnMouseEntered(createEventHandler("advanced", "default", defaultBoard));
        advancedBoard.setOnMouseEntered(createEventHandler("default", "advanced", advancedBoard));
    }

    private EventHandler<MouseEvent> createEventHandler(String removeClass, String addClass, Hyperlink parent) {
        return event -> {
            content.getStyleClass().remove(removeClass);
            content.getStyleClass().add(addClass);
            popOver.show(parent);
        };
    }

}
