package me.belakede.thesis.client.boundary.javafx.control;


import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.boundary.javafx.task.RemoveGameTask;
import me.belakede.thesis.client.boundary.javafx.task.StartGameTask;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.Status;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.time.LocalDateTime;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class GameDetailsPane extends BorderPane {

    private final LongProperty gameId = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> created = new SimpleObjectProperty<>();
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final BooleanProperty removed = new SimpleBooleanProperty(false);
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>();

    @FXML
    private Text idText;
    @FXML
    private Text createdText;
    @FXML
    private Glyph statusGlyph;
    @FXML
    private VBox boardBox;
    @FXML
    private FlowPane playersPane;
    @FXML
    private Button remove;
    @FXML
    private Button start;
    @FXML
    private Button join;

    public GameDetailsPane(GameSummary game, BooleanProperty removed) {
        load(this);
        setupActionEvents();
        setupBinginds(removed);
        hookupChangeListeners();
        setGameId(game.getId());
        setCreated(game.getCreated());
        setBoardType(game.getBoardType());
        setPlayers(game.getPlayers());
        setStatus(game.getStatus());
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

    public Status getStatus() {
        return status.get();
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
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
        remove.setOnAction(event -> {
            Task<Void> task = new RemoveGameTask(getGameId());
            task.setOnSucceeded(e -> {
                start.setDisable(true);
                removed.set(true);
                Text removedText = new Text("Removed");
                removedText.getStyleClass().addAll("removed");
                removedText.setRotate(45);
                boardBox.getChildren().add(removedText);
            });
            new Thread(task).start();
        });
        start.setOnAction(event -> {
            Task<Void> task = new StartGameTask(getGameId());
            task.setOnSucceeded(e -> {
                remove.setDisable(true);
                start.setDisable(true);
                start.setVisible(false);
                join.setDisable(false);
                join.setVisible(true);
            });
            task.setOnFailed(e -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Another game is already running!", ButtonType.OK);
                alert.setTitle("Game starting error");
                alert.setHeaderText("Can't start the specified game because an other game is already running.");
                alert.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.EXCLAMATION_CIRCLE));
                alert.showAndWait();
            });
            new Thread(task).start();
        });
    }

    private void setupBinginds(BooleanProperty removed) {
        idText.textProperty().bind(gameIdProperty().asString("#%d"));
        createdText.textProperty().bind(createdProperty().asString());
        start.visibleProperty().bind(start.disableProperty().not());
        start.disableProperty().bind(statusProperty().isEqualTo(Status.IN_PROGRESS));
        join.visibleProperty().bind(start.disableProperty());
        join.disableProperty().bind(Bindings.createBooleanBinding(() -> playersProperty().getValue().values().contains(UserConfiguration.getInstance().getUsername()), playersProperty()).not());
        removed.bind(this.removed);
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
        statusProperty().addListener((observable, oldValue, newValue) -> {
            statusGlyph.setIcon(statusToIcon(newValue));
            if (Status.IN_PROGRESS.equals(newValue)) {
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(3000), statusGlyph);
                rotateTransition.setToAngle(360);
                rotateTransition.setCycleCount(Timeline.INDEFINITE);
                rotateTransition.play();
                double x = statusGlyph.widthProperty().divide(2).doubleValue();
                double y = statusGlyph.heightProperty().divide(2).doubleValue();
                statusGlyph.getTransforms().add(new Translate(-x, -y));
                statusGlyph.setTranslateX(x);
                statusGlyph.setTranslateY(y);
            }
        });
    }

    private FontAwesome.Glyph statusToIcon(Status status) {
        switch (status) {
            case CREATED:
                return FontAwesome.Glyph.ASTERISK;
            case FINISHED:
                return FontAwesome.Glyph.STOP;
            case IN_PROGRESS:
                return FontAwesome.Glyph.SPINNER;
            case PAUSED:
                return FontAwesome.Glyph.PAUSE;
            default:
                return FontAwesome.Glyph.EXCLAMATION_TRIANGLE;
        }
    }

}
