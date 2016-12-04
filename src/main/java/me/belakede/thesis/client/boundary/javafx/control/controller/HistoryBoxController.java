package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import me.belakede.thesis.client.service.NotificationConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class HistoryBoxController implements Initializable {

    private final NotificationConverterService notificationConverterService;

    @FXML
    private ListView<String> eventList;

    @Autowired
    public HistoryBoxController(NotificationConverterService notificationConverterService) {
        this.notificationConverterService = notificationConverterService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindEvents();
    }

    private void bindEvents() {
        eventList.itemsProperty().bind(notificationConverterService.notificationsProperty());
    }
}
