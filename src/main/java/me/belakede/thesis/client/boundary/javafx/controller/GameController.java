package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.ChatPane;
import me.belakede.thesis.client.boundary.javafx.control.GamePane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryPane;
import me.belakede.thesis.client.boundary.javafx.control.NotePane;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.SnapshotService;
import me.belakede.thesis.server.game.response.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class GameController implements Initializable {

    private final NotificationService notificationService;
    private final SnapshotService snapshotService;

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
    public GameController(NotificationService notificationService, SnapshotService snapshotService) {
        this.notificationService = notificationService;
        this.snapshotService = snapshotService;
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
            gamePane.setDisable(true);
            notePane.setDisable(true);
            gamePane.setVisible(false);
            notePane.setVisible(false);
            sorryPane.setVisible(true);
        });
    }

}
