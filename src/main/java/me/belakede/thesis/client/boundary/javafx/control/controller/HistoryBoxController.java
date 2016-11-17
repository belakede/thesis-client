package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.server.game.response.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class HistoryBoxController implements Initializable {

    private final NotificationService notificationService;

    @FXML
    private ListView<Notification> eventList;

    @Autowired
    public HistoryBoxController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindEvents();
    }

    private void bindEvents() {
        eventList.itemsProperty().bind(notificationService.notificationsProperty());
    }
}
