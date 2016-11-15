package me.belakede.thesis.client.boundary.javafx.control;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.boundary.javafx.task.CreateGameTask;
import me.belakede.thesis.client.boundary.javafx.task.DownloadGamesTask;
import me.belakede.thesis.game.equipment.BoardType;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.util.Optional;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class GamesPane extends VBox {

    private final ListProperty<GameSummary> games = new SimpleListProperty<>();
    private final ListProperty<String> players = new SimpleListProperty<>();
    private final ObjectProperty<GameSummary> selectedGame = new SimpleObjectProperty<>();

    @FXML
    private VBox parent;
    @FXML
    private ListView<GameSummary> gamesView;
    @FXML
    private Button createButton;
    @FXML
    private Button refreshButton;

    public GamesPane() {
        load(this);
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

    private void setupActionEvents() {
        createButton.setOnAction(event -> createGame());
        refreshButton.setOnAction(event -> downloadGames());
    }

    private void hookupChangeListeners() {
        gamesView.itemsProperty().bind(games);
        gamesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedGame.bind(gamesView.getSelectionModel().selectedItemProperty());
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
