package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    @FXML
    private VBox parent;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void hide(ActionEvent actionEvent) {
        TranslateTransition transition = new TranslateTransition(new Duration(350), parent);
        transition.setToY(-(parent.getHeight()));
        transition.play();
    }
}
