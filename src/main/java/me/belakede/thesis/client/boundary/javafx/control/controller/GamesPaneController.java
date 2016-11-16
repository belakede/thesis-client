package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.boundary.javafx.task.CreateGameTask;
import me.belakede.thesis.client.boundary.javafx.task.DownloadGamesTask;
import me.belakede.thesis.game.equipment.BoardType;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class GamesPaneController implements Initializable {

    private final ListProperty<GameSummary> games = new SimpleListProperty<>();
    private final ListProperty<String> players = new SimpleListProperty<>();
    private final ObjectProperty<GameSummary> selectedGame = new SimpleObjectProperty<>();
    private final BooleanProperty removed = new SimpleBooleanProperty(false);
    private final DownloadGamesTask downloadGamesTask;

    @FXML
    private ListView<GameSummary> gamesView;

    @FXML
    private Button createButton;
    @FXML
    private Button refreshButton;

    @Autowired
    public GamesPaneController(DownloadGamesTask downloadGamesTask) {
        this.downloadGamesTask = downloadGamesTask;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupActionEvents();
        hookupChangeListeners();
        downloadGames();
    }

    public ObservableList<GameSummary> getGames() {
        return games.get();
    }

    public void setGames(ObservableList<GameSummary> games) {
        this.games.set(games);
    }

    public ListProperty<GameSummary> gamesProperty() {
        return games;
    }

    public ObservableList<String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableList<String> players) {
        this.players.set(players);
    }

    public ListProperty<String> playersProperty() {
        return players;
    }

    public GameSummary getSelectedGame() {
        return selectedGame.get();
    }

    public void setSelectedGame(GameSummary selectedGame) {
        this.selectedGame.set(selectedGame);
    }

    public ObjectProperty<GameSummary> selectedGameProperty() {
        return selectedGame;
    }

    public boolean isRemoved() {
        return removed.get();
    }

    public void setRemoved(boolean removed) {
        this.removed.set(removed);
    }

    public BooleanProperty removedProperty() {
        return removed;
    }

    private void setupActionEvents() {
        createButton.setOnAction(event -> createGame());
        refreshButton.setOnAction(event -> downloadGames());
    }

    private void hookupChangeListeners() {
        gamesView.itemsProperty().bind(games);
        gamesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedGame.bind(gamesView.getSelectionModel().selectedItemProperty());
        removed.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                games.remove(getSelectedGame());
            }
        });
    }

    private void downloadGames() {
        Task<ObservableList<GameSummary>> task = new DownloadGamesTask();
        task.setOnSucceeded(event -> games.setValue(task.getValue()));
        Thread thread = new Thread(task);
        thread.start();
    }

    private void createGame() {
        Optional<BoardType> optionalBoardType = selectBoardType();
        if (optionalBoardType.isPresent()) {
            Optional<ObservableList<String>> optionalPlayers = addPlayers();
            if (optionalPlayers.isPresent()) {
                Task<GameSummary> task = new CreateGameTask(optionalBoardType.get(), optionalPlayers.get());
                task.setOnSucceeded(event -> games.add(task.getValue()));
                new Thread(task).start();
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

        ListView<String> players = new ListView<>(getPlayers());
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
            return null;
        });

        return dialog.showAndWait();
    }

}
