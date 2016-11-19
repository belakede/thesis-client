package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
import me.belakede.thesis.client.boundary.javafx.control.SuggestionPane;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.UserService;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ActionPaneController implements Initializable {

    private final UserService userService;
    private final NotificationService notificationService;

    @FXML
    private Button roll;
    @FXML
    private Button suspect;
    @FXML
    private Button show;
    @FXML
    private Button accuse;
    private CardPane cardPane;
    private PopOver cardPopOver;
    private PopOver suspectPopOver;
    private PopOver accusePopOver;

    @Autowired
    public ActionPaneController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCardPane();
        setupCardPopOver();
        setupSuspectPopOver();
        setupAccusePopOver();
        setupActionEvents();
        initButtons();
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        notificationService.currentPlayerNotificationProperty().addListener((observable, oldValue, newValue) -> {
            toggleButtons(userService.getUsername().equals(newValue.getCurrent()));
        });
    }

    private void setupCardPane() {
        cardPane = new CardPane();
    }


    private void setupCardPopOver() {
        cardPopOver = new PopOver(cardPane);
        setupPopOver(cardPopOver);
    }

    private void setupSuspectPopOver() {
        suspectPopOver = new PopOver(new SuggestionPane(SuggestionPane.Type.SUSPECT));
        setupPopOver(suspectPopOver);
    }

    private void setupAccusePopOver() {
        accusePopOver = new PopOver(new SuggestionPane(SuggestionPane.Type.ACCUSE));
        setupPopOver(accusePopOver);
    }

    private void setupActionEvents() {
        suspect.setOnAction(event -> suspectPopOver.show(suspect));
        show.setOnAction(event -> cardPopOver.show(show));
        accuse.setOnAction(event -> accusePopOver.show(accuse));
    }

    private void initButtons() {
        if (notificationService.getCurrentPlayerNotification() != null) {
            toggleButtons(userService.getUsername().equals(notificationService.getCurrentPlayerNotification().getCurrent()));
        }
    }

    private void setupPopOver(PopOver popOver) {
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        popOver.setDetachable(false);
        popOver.setDetached(false);
    }

    private void toggleButtons(boolean value) {
        roll.setDisable(value);
        suspect.setDisable(value);
        show.setDisable(value);
        accuse.setDisable(value);
    }
}
