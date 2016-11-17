package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.service.RemoveGameService;
import me.belakede.thesis.client.boundary.javafx.service.StartGameService;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.domain.Status;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

@Service
@Scope("prototype")
public class GameDetailsPaneController implements Initializable {


    private final LongProperty gameId = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> created = new SimpleObjectProperty<>();
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>();

    private final BooleanProperty removed = new SimpleBooleanProperty(false);
    private final StartGameService startGameService;
    private final RemoveGameService removeGameService;

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

    @Autowired
    public GameDetailsPaneController(StartGameService startGameService, RemoveGameService removeGameService) {
        this.startGameService = startGameService;
        this.removeGameService = removeGameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupServices();
        setupActionEvents();
        setupBindings();
        hookupChangeListeners();
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

    private void setupServices() {
        removeGameService.setOnSucceeded(event -> {
            setRemoved(true);
            addRemovedText();
        });
        removeGameService.setOnFailed(event -> showAndWaitAlert("Game deletion error", "Can't remove the specified game because an other game is still running."));
        startGameService.setOnSucceeded(event -> setStatus(Status.IN_PROGRESS));
        startGameService.setOnFailed(event -> showAndWaitAlert("Game starting error", "Can't start the specified game because an other game is already running."));
    }

    private void setupActionEvents() {
        remove.setOnAction(event -> {
            removeGameService.setId(getGameId());
            removeGameService.restart();
        });
        start.setOnAction(event -> {
            startGameService.setId(getGameId());
            startGameService.restart();
        });
    }

    private void setupBindings() {
        idText.textProperty().bind(gameIdProperty().asString("#%d"));
        createdText.textProperty().bind(createdProperty().asString());
        start.visibleProperty().bind(start.disableProperty().not().or(removedProperty()));
        start.disableProperty().bind(statusProperty().isEqualTo(Status.IN_PROGRESS).or(removedProperty()));
        remove.disableProperty().bind(statusProperty().isEqualTo(Status.IN_PROGRESS).or(removedProperty()));
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
                rotateStatusGlyph();
            }
        });
    }

    private void addRemovedText() {
        Text removedText = new Text("Removed");
        removedText.getStyleClass().addAll("removed");
        removedText.setRotate(45);
        boardBox.getChildren().add(removedText);
    }

    private void showAndWaitAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Game in progress!", ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.EXCLAMATION_CIRCLE));
        alert.showAndWait();
    }

    private void rotateStatusGlyph() {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(2500), statusGlyph);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setDelay(Duration.ZERO);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.play();
    }

    private FontAwesome.Glyph statusToIcon(Status status) {
        switch (status) {
            case CREATED:
                return FontAwesome.Glyph.ASTERISK;
            case FINISHED:
                return FontAwesome.Glyph.STOP;
            case IN_PROGRESS:
                return FontAwesome.Glyph.CIRCLE_ALT_NOTCH;
            case PAUSED:
                return FontAwesome.Glyph.PAUSE;
            default:
                return FontAwesome.Glyph.EXCLAMATION_TRIANGLE;
        }
    }

}
