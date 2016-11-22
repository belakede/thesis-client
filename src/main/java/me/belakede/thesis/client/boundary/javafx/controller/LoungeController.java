package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.service.DownloadNotesService;
import me.belakede.thesis.client.service.*;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.response.PlayerJoinedNotification;
import org.controlsfx.control.ToggleSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoungeController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoungeController.class);

    private final MapProperty<String, VBox> playerBoxes = new SimpleMapProperty<>();
    private final UserService userService;
    private final GameService gameService;
    private final GameFlowService gameFlowService;
    private final SnapshotService snapshotService;
    private final NotificationService notificationService;
    private final DownloadNotesService downloadNotesService;

    @FXML
    private VBox parent;
    @FXML
    private StackPane sorryPane;
    @FXML
    private HBox playerContainer;
    @FXML
    private ToggleSwitch recordingSwitch;

    @Autowired
    public LoungeController(UserService userService, GameService gameService, GameFlowService gameFlowService, SnapshotService snapshotService, NotificationService notificationService, DownloadNotesService downloadNotesService) {
        this.userService = userService;
        this.gameService = gameService;
        this.gameFlowService = gameFlowService;
        this.snapshotService = snapshotService;
        this.notificationService = notificationService;
        this.downloadNotesService = downloadNotesService;
    }

    public ObservableMap<String, VBox> getPlayerBoxes() {
        return playerBoxes.get();
    }

    public void setPlayerBoxes(ObservableMap<String, VBox> playerBoxes) {
        this.playerBoxes.set(playerBoxes);
    }

    public MapProperty<String, VBox> playerBoxesProperty() {
        return playerBoxes;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPlayerBoxes(FXCollections.observableHashMap());
        uploadPlayerBoxes();
        if (isPartOfGame()) {
            hookupChangeListeners();
            openChannels();
            downloadNotes();
        } else {
            displaySorryPane();
        }
    }

    private void uploadPlayerBoxes() {
        gameService.getPlayers().entrySet().forEach(entry -> {
            LOGGER.info("Creating playerBox for {}", entry);
            playerBoxes.put(entry.getValue(), createVBoxForUser(entry.getValue(), entry.getKey()));
        });
        playerContainer.getChildren().addAll(playerBoxes.values());
    }

    private boolean isPartOfGame() {
        return gameService.getPlayers().containsValue(userService.getUsername());
    }

    private void displaySorryPane() {
        sorryPane.setVisible(true);
    }

    private void hookupChangeListeners() {
        recordingSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            snapshotService.setEnabled(newValue);
        });
        playerBoxesProperty().addListener(new MapChangeListener<String, VBox>() {
            @Override
            public void onChanged(Change<? extends String, ? extends VBox> change) {
                if (change.wasAdded()) {
                    playerContainer.getChildren().add(change.getValueAdded());
                }
            }
        });
        notificationService.playerStatusNotificationProperty().addListener((observable, oldValue, newValue) -> {
            newValue.getAlreadyWaiting().forEach(player -> getPlayerBoxes().get(player).getStyleClass().add("online"));
        });
        notificationService.playerJoinedNotificationsProperty().addListener((ListChangeListener.Change<? extends PlayerJoinedNotification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(notification -> getPlayerBoxes().get(notification.getUser()).getStyleClass().add("online"));
                }
            }
        });
        notificationService.gameStatusNotificationProperty().addListener((observable, oldValue, newValue) -> {
            // TODO wait 5 second - count down.
            hide();
        });
    }

    private void openChannels() {
        gameFlowService.openChannels();
    }

    private void downloadNotes() {
        downloadNotesService.start();
    }

    private VBox createVBoxForUser(String username, Suspect figurine) {
        VBox result = new VBox();
        result.getStyleClass().addAll("player", figurine.name().toLowerCase());
        if (userService.getUsername().equals(username)) {
            result.getStyleClass().add("online");
        }
        result.getChildren().addAll(createIconForUser(), createTextForUser(username));
        return result;
    }

    private VBox createIconForUser() {
        VBox result = new VBox();
        result.getStyleClass().add("icon");
        return result;
    }

    private Text createTextForUser(String username) {
        return new Text(username);
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.setOnFinished(event -> parent.setVisible(false));
        transition.play();
    }
}
