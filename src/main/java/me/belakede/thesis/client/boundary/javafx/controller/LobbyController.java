package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.GameDetailsPane;
import me.belakede.thesis.client.boundary.javafx.control.GamesPane;
import me.belakede.thesis.client.boundary.javafx.control.PlayersPane;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    private final Map<GameSummary, GameDetailsPane> cache = new HashMap<>();

    @FXML
    private VBox parent;
    @FXML
    private VBox content;
    @FXML
    private PlayersPane playersPane;
    @FXML
    private GamesPane gamesPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupBindings();
        hookupChangeListeners();
    }

    private void setupBindings() {
        gamesPane.playersProperty().bind(playersPane.playersProperty());
    }

    private void hookupChangeListeners() {
        gamesPane.selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                content.getChildren().clear();
                if (!cache.containsKey(newValue)) {
                    cache.put(newValue, new GameDetailsPane(newValue, gamesPane.removedProperty()));
                }
                content.getChildren().add(cache.get(newValue));
            }
        });
    }

    public void join(ActionEvent event) {
        hide();
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.setOnFinished(event -> parent.setVisible(false));
        transition.play();
    }

}
