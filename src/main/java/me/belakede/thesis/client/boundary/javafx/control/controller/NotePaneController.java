package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.NoteBox;
import me.belakede.thesis.client.service.PlayerService;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NotePaneController implements Initializable {

    private final PlayerService playerService;
    private ResourceBundle resources;

    @FXML
    private StackPane parent;
    @FXML
    private Button noteButton;
    private PopOver popOver;

    @Autowired
    public NotePaneController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResources(resources);
        setupPopover();
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        playerService.figurineProperty().addListener((observable, oldValue, newValue) -> {
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
        popOver.setTitle(getResources().getString("Notes"));
        popOver.setDetached(false);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
    }

    private ResourceBundle getResources() {
        return resources;
    }

    private void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
