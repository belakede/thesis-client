package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    @FXML
    private StackPane parent;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadContent("game");
            loadContent("auth");
        } catch (IOException e) {
            throw new RuntimeException("Can't load auth.fxml");
        }
    }

    private void loadContent(String key) throws IOException {
        Pane load = FXMLLoader.load(getClass().getResource("../" + key + ".fxml"));
        parent.getChildren().add(load);
    }
}
