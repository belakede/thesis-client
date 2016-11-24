package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryBox;
import me.belakede.thesis.client.service.PlayerService;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class HistoryPaneController implements Initializable {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final PlayerService playerService;
    @FXML
    public StackPane parent;
    private ResourceBundle resources;
    @FXML
    private Button historyButton;
    private PopOver popOver;

    @Autowired
    public HistoryPaneController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResources(resources);
        setupPopover();
        hookupChangeListeners();
        setupBindings();
    }

    private void setupPopover() {
        popOver = new PopOver(new HistoryBox());
        popOver.setAnimated(true);
        popOver.setTitle(getResources().getString("History"));
        popOver.setHeaderAlwaysVisible(true);
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                historyButton.getStyleClass().remove(oldValue.name().toLowerCase());
            }
            historyButton.getStyleClass().add(newValue.name().toLowerCase());
        });
        historyButton.setOnAction(event -> popOver.show(parent));
    }

    private void setupBindings() {
        figurine.bind(playerService.figurineProperty());
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
