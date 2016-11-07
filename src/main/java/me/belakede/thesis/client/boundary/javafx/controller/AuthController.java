package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    @FXML
    private GridPane parent;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void submit(ActionEvent actionEvent) {
        hide();
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.play();
    }
}
