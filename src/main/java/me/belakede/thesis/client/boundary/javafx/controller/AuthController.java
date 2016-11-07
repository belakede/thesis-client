package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    @FXML
    private VBox parent;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void hide(ActionEvent actionEvent) {
        parent.setVisible(false);
    }
}
