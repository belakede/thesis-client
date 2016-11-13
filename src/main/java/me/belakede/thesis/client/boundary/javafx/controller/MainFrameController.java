package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    @FXML
    private StackPane parent;

    public void initialize(URL location, ResourceBundle resources) {
        nestedLoader("auth", Optional.of("game"));
    }

    private void nestedLoader(String first, Optional<String> second) {
        try {
            LOGGER.info("Try to load {}", first);
            Pane pane = loadContent(first);
            parent.getChildren().add(pane);
            if (second.isPresent()) {
                pane.visibleProperty().addListener((observable, oldValue, newValue) -> {
                    parent.getChildren().remove(pane);
                    nestedLoader(second.get(), Optional.empty());
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't load fxml: ", e);
        }
    }

    private Pane loadContent(String key) throws IOException {
        return FXMLLoader.load(getClass().getResource("../" + key + ".fxml"));
    }
}
