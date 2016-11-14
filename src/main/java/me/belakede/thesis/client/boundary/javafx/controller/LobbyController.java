package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.GameDetailsPane;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.game.equipment.BoardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    @FXML
    private ListView<GameSummary> games;

    @FXML
    private VBox parent;
    @FXML
    private VBox content;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uploadGames();
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        games.getSelectionModel().selectionModeProperty().set(SelectionMode.SINGLE);
        games.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Selection: {}", newValue);
            content.getChildren().clear();
            content.getChildren().add(new GameDetailsPane(newValue.getId(), newValue.getCreated(), newValue.getBoardType(), newValue.getPlayers()));
        });
    }

    private void uploadGames() {
        games.getItems().add(new GameSummary(1L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.ADVANCED, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.ADVANCED, FXCollections.observableArrayList("Player 1", "Player 2", "Player 4", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.ADVANCED, FXCollections.observableArrayList("Player 3", "Player 4", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4")));
        games.getItems().add(new GameSummary(2L, LocalDateTime.now(), BoardType.DEFAULT, FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6")));
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

    public void create(ActionEvent actionEvent) {

    }
}
