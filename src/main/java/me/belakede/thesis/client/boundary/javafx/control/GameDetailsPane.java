package me.belakede.thesis.client.boundary.javafx.control;


import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.boundary.javafx.task.RemoveGameTask;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;

import java.time.LocalDateTime;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class GameDetailsPane extends BorderPane {

    private final LongProperty gameId = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> created = new SimpleObjectProperty<>();
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();

    @FXML
    private Text idText;
    @FXML
    private Text createdText;
    @FXML
    private VBox boardBox;
    @FXML
    private FlowPane playersPane;
    @FXML
    private Button remove;
    @FXML
    private Button start;

    public GameDetailsPane(GameSummary game) {
        this(game.getId(), game.getCreated(), game.getBoardType(), game.getPlayers());
    }

    public GameDetailsPane(long gameId, LocalDateTime created, BoardType boardType, ObservableMap<Suspect, String> players) {
        load(this);
        setupActionEvents();
        setupBinginds();
        hookupChangeListeners();
        setGameId(gameId);
        setCreated(created);
        setBoardType(boardType);
        setPlayers(players);
    }


    public long getGameId() {
        return gameId.get();
    }

    public void setGameId(long gameId) {
        this.gameId.set(gameId);
    }

    public LongProperty gameIdProperty() {
        return gameId;
    }

    public LocalDateTime getCreated() {
        return created.get();
    }

    public void setCreated(LocalDateTime created) {
        this.created.set(created);
    }

    public ObjectProperty<LocalDateTime> createdProperty() {
        return created;
    }

    public BoardType getBoardType() {
        return boardType.get();
    }

    public void setBoardType(BoardType boardType) {
        this.boardType.set(boardType);
    }

    public ObjectProperty<BoardType> boardTypeProperty() {
        return boardType;
    }

    public ObservableMap<Suspect, String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableMap<Suspect, String> players) {
        this.players.set(players);
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
    }

    private void setupActionEvents() {
        remove.setOnAction(event -> {
            Task<Void> task = new RemoveGameTask(getGameId());
            task.setOnSucceeded(e -> {
                start.setDisable(true);
                Text removed = new Text("Removed");
                removed.getStyleClass().addAll("removed");
                removed.setRotate(45);
                boardBox.getChildren().add(removed);
            });
            new Thread(task).start();
        });
    }

    private void setupBinginds() {
        idText.textProperty().bind(gameIdProperty().asString("#%d"));
        createdText.textProperty().bind(createdProperty().asString());
    }

    private void hookupChangeListeners() {
        boardTypeProperty().addListener((observable, oldValue, newValue) -> {
            boardBox.getStyleClass().removeAll("default", "advanced");
            boardBox.getStyleClass().add(newValue.name().toLowerCase());
        });
        playersProperty().addListener((observable, oldValue, newValue) -> newValue.entrySet().stream().map(player -> {
            Text text = new Text(player.getValue());
            text.getStyleClass().addAll("player", player.getKey().name().toLowerCase());
            return text;
        }).forEach(player -> playersPane.getChildren().add(player)));
    }

}
