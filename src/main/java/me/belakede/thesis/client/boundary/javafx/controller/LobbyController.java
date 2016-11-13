package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    @FXML
    private VBox parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
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
