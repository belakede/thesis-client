package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.boundary.javafx.service.DownloadGamesService;
import me.belakede.thesis.client.boundary.javafx.task.CreateGameTask;
import me.belakede.thesis.client.service.UserService;
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
    private final UserService userService;
    private final DownloadGamesService downloadGamesService;
    private ResourceBundle resourceBundle;

    @FXML
    private ListView<GameSummary> gamesView;

    @FXML
    private Button createButton;
    @FXML
    private Button refreshButton;

    @Autowired
    public GamesPaneController(UserService userService, DownloadGamesService downloadGamesService) {
        this.userService = userService;
        this.downloadGamesService = downloadGamesService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
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

    public void remove(GameSummary gameSummary) {
        if (getGames().contains(gameSummary)) {
            getGames().remove(gameSummary);
            downloadGamesService.restart();
        }
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
        refreshButton.setOnAction(event -> downloadGamesService.restart());
    }

    private void hookupChangeListeners() {
        gamesView.itemsProperty().bind(games);
        gamesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedGame.bind(gamesView.getSelectionModel().selectedItemProperty());
    }

    private void downloadGames() {
        downloadGamesService.setOnSucceeded(event -> games.setValue(downloadGamesService.getValue()));
        if (games.isEmpty()) {
            downloadGamesService.start();
        }
    }

    private void createGame() {
        Optional<BoardType> optionalBoardType = selectBoardType();
        if (optionalBoardType.isPresent()) {
            Optional<ObservableList<String>> optionalPlayers = addPlayers();
            if (optionalPlayers.isPresent()) {
                CreateGameTask task = new CreateGameTask(userService);
                task.setBoardType(optionalBoardType.get());
                task.setPlayers(optionalPlayers.get());
                task.setOnSucceeded(event -> games.add(task.getValue()));
                new Thread(task).start();
            }
        }
    }

    private Optional<BoardType> selectBoardType() {
        ChoiceDialog<BoardType> dialog = new ChoiceDialog<>(BoardType.DEFAULT, BoardType.values());
        dialog.setTitle(getResourceBundle().getString("Board Type"));
        dialog.setHeaderText(getResourceBundle().getString("Select board type"));
        dialog.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.AREA_CHART));
        dialog.setContentText(getResourceBundle().getString("Choose board type:"));
        return dialog.showAndWait();
    }

    private Optional<ObservableList<String>> addPlayers() {
        Dialog<ObservableList<String>> dialog = new Dialog<>();
        dialog.setTitle(getResourceBundle().getString("Players"));
        dialog.setHeaderText(getResourceBundle().getString("Select minimum 2 players for the game"));
        dialog.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.USERS));
        ButtonType addButtonType = new ButtonType(getResourceBundle().getString("Add"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(getResourceBundle().getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        ListView<String> players = new ListView<>(getPlayers());
        players.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Node loginButton = dialog.getDialogPane().lookupButton(addButtonType);
        loginButton.setDisable(true);

        players.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            loginButton.setDisable(players.getSelectionModel().getSelectedItems().size() < 2 || players.getSelectionModel().getSelectedItems().size() > 6);
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

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
