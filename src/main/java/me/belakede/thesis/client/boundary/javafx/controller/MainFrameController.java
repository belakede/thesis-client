package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    @FXML
    private StackPane parent;

    public void initialize(URL location, ResourceBundle resources) {
        nestedLoader("auth", ((observable, oldValue, newValue)
                -> nestedLoader("lobby", (observable1, oldValue1, newValue1)
                -> nestedLoader("lounge", (observable2, oldValue2, newValue2)
                -> nestedLoader("game", (observable3, oldValue3, newValue3)
                -> Platform.exit())))));
    }

    private void nestedLoader(String name, ChangeListener<Boolean> changeListener) {
        LOGGER.info("Loading {}", name);
        Pane pane = loadContent(name);
        parent.getChildren().clear();
        parent.getChildren().add(pane);
        pane.visibleProperty().addListener(changeListener);
    }

    private Pane loadContent(String key) {
        try {
            return FXMLLoader.load(getClass().getResource("../" + key + ".fxml"));
        } catch (IOException e) {
            throw new RuntimeException("Can't load fxml: ", e);
        }
    }
}
