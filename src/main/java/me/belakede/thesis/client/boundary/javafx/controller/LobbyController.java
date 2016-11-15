package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.GamesPane;
import me.belakede.thesis.client.boundary.javafx.control.PlayersPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

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
    }

    private void setupBindings() {
        gamesPane.playersProperty().bind(playersPane.playersProperty());
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
