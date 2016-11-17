package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryBox;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class HistoryPaneController implements Initializable {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final GameService gameService;

    @FXML
    public StackPane parent;
    @FXML
    private Button historyButton;
    private PopOver popOver;

    @Autowired
    public HistoryPaneController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPopover();
        hookupChangeListeners();
        setupBindings();
    }

    private void setupPopover() {
        popOver = new PopOver(new HistoryBox());
        popOver.setAnimated(true);
        popOver.setTitle("History");
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
        figurine.bind(gameService.figurineProperty());
    }
}
