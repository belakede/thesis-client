package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.GameDetailsPane;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.game.equipment.BoardType;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
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
        Optional<BoardType> optionalBoardType = selectBoardType();
        if (optionalBoardType.isPresent()) {
            Optional<ObservableList<String>> optionalPlayers = addPlayers();
            if (optionalBoardType.isPresent()) {
                games.getItems().add(new GameSummary(1L, LocalDateTime.now(), optionalBoardType.get(), optionalPlayers.get()));
            }
        }
    }

    private Optional<BoardType> selectBoardType() {
        ChoiceDialog<BoardType> dialog = new ChoiceDialog<>(BoardType.DEFAULT, BoardType.values());
        dialog.setTitle("Board Type");
        dialog.setHeaderText("Select board type");
        dialog.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.AREA_CHART));
        dialog.setContentText("Choose your letter:");
        return dialog.showAndWait();
    }

    private Optional<ObservableList<String>> addPlayers() {
        Dialog<ObservableList<String>> dialog = new Dialog<>();
        dialog.setTitle("Players");
        dialog.setHeaderText("Select minimum 2 players for the game");
        dialog.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.USERS));
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        ListView<String> players = new ListView<>(FXCollections.observableArrayList("Player 1", "Player 2", "Player 3"));
        players.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Node loginButton = dialog.getDialogPane().lookupButton(addButtonType);
        loginButton.setDisable(true);

        players.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            loginButton.setDisable(players.getSelectionModel().getSelectedItems().size() < 2);
        });

        dialog.getDialogPane().setContent(players);

        Platform.runLater(players::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return players.getSelectionModel().getSelectedItems();
            }
            return FXCollections.emptyObservableList();
        });

        return dialog.showAndWait();
    }

}
