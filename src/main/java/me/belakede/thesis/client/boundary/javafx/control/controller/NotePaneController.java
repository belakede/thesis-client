package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.NoteBox;
import me.belakede.thesis.client.service.GameService;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NotePaneController implements Initializable {

    private final GameService gameService;

    @FXML
    private StackPane parent;
    @FXML
    private Button noteButton;
    private PopOver popOver;

    @Autowired
    public NotePaneController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPopover();
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        gameService.figurineProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                noteButton.getStyleClass().remove(oldValue.name().toLowerCase());
            }
            noteButton.getStyleClass().add(newValue.name().toLowerCase());
        });
        noteButton.setOnAction(event -> popOver.show(parent));
    }

    private void setupPopover() {
        popOver = new PopOver(new NoteBox());
        popOver.setAnimated(true);
        popOver.setTitle("Notes");
        popOver.setDetached(false);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
    }

}
