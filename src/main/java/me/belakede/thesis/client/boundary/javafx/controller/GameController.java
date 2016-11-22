package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.control.ChatPane;
import me.belakede.thesis.client.boundary.javafx.control.GamePane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryPane;
import me.belakede.thesis.client.boundary.javafx.control.NotePane;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.SnapshotService;
import me.belakede.thesis.server.game.response.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

@Controller
public class GameController implements Initializable {

    private final GameService gameService;
    private final SnapshotService snapshotService;
    private final LoungeController loungeController;
    private final NotificationService notificationService;
    @FXML
    public StackPane endPane;
    @FXML
    public HBox playerContainer;
    @FXML
    private AnchorPane parent;
    @FXML
    private GamePane gamePane;
    @FXML
    private NotePane notePane;
    @FXML
    private HistoryPane historyPane;
    @FXML
    private ChatPane chatPane;
    @FXML
    private StackPane sorryPane;

    @Autowired
    public GameController(GameService gameService, SnapshotService snapshotService, LoungeController loungeController, NotificationService notificationService) {
        this.gameService = gameService;
        this.snapshotService = snapshotService;
        this.loungeController = loungeController;
        this.notificationService = notificationService;
    }

    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        notificationService.notificationsProperty().addListener((ListChangeListener.Change<? extends Notification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(o -> snapshotService.saveAsPng(parent));
                }
            }
        });
        notificationService.gamePausedNotificationProperty().addListener((observable, oldValue, newValue) -> {
            disablePanes();
            if (notificationService.getGameEndedNotification() == null) {
                sorryPane.setVisible(true);
            }
        });
        notificationService.gameEndedNotificationProperty().addListener((observable, oldValue, newValue) -> {
            disablePanes();
            final StringProperty winnerClass = new SimpleStringProperty();
            String winner = newValue.getWinner();
            if (winner != null) {
                winnerClass.setValue(gameService.getReversedPlayers().get(winner).name().toLowerCase());
            }
            Collection<VBox> playerBoxes = loungeController.getPlayerBoxes().values();
            playerBoxes.stream()
                    .filter(playerBox -> winner == null || !playerBox.getStyleClass().contains(winnerClass.getValue()))
                    .forEach(playerBox -> playerBox.getStyleClass().removeAll(Collections.singleton("online")));
            Platform.runLater(() -> {
                playerContainer.getChildren().clear();
                playerContainer.getChildren().addAll(playerBoxes);
                endPane.setVisible(true);
            });
        });
    }

    private void disablePanes() {
        gamePane.setDisable(true);
        notePane.setDisable(true);
        gamePane.setVisible(false);
        notePane.setVisible(false);
    }

}
