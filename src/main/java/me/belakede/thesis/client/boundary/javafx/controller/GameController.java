package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.ChatPane;
import me.belakede.thesis.client.boundary.javafx.control.GamePane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryPane;
import me.belakede.thesis.client.boundary.javafx.control.NotePane;
import me.belakede.thesis.client.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class GameController implements Initializable {

    private final NotificationService notificationService;

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
    public GameController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        notificationService.gamePausedNotificationProperty().addListener((observable, oldValue, newValue) -> {
            gamePane.setDisable(true);
            notePane.setDisable(true);
            gamePane.setVisible(false);
            notePane.setVisible(false);
            sorryPane.setVisible(true);
        });
    }

}
