package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.collections.MapChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
import me.belakede.thesis.client.boundary.javafx.control.SuggestionPane;
import me.belakede.thesis.client.boundary.javafx.task.NextTask;
import me.belakede.thesis.client.boundary.javafx.task.QuitTask;
import me.belakede.thesis.client.boundary.javafx.task.RollTask;
import me.belakede.thesis.client.service.*;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ActionPaneController implements Initializable {

    private final UserService userService;
    private final RollService rollService;
    private final PlayerService playerService;
    private final PositionService positionService;
    private final NotificationService notificationService;

    @FXML
    private Button roll;
    @FXML
    private Button suspect;
    @FXML
    private Button show;
    @FXML
    private Button accuse;
    @FXML
    private Button next;
    @FXML
    private Button quit;
    private CardPane cardPane;
    private PopOver cardPopOver;
    private PopOver suspectPopOver;
    private PopOver accusePopOver;

    @Autowired
    public ActionPaneController(UserService userService, RollService rollService, PlayerService playerService, PositionService positionService, NotificationService notificationService) {
        this.userService = userService;
        this.rollService = rollService;
        this.playerService = playerService;
        this.positionService = positionService;
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCardPane();
        setupCardPopOver();
        setupSuspectPopOver();
        setupAccusePopOver();
        setupActionEvents();
        hookupChangeListeners();
        initButtons();
    }

    private void hookupChangeListeners() {
        playerService.currentProperty().addListener((observable, oldValue, newValue) -> {
            toggleButtons(!newValue);
        });
        rollService.secondProperty().addListener((observable, oldValue, newValue) -> {
            roll.setDisable(true);
        });
        positionService.positionsProperty().addListener((Change<? extends Figurine, ? extends Field> change) -> {
            if (change.wasAdded() && change.getKey().equals(playerService.getFigurine())) {
                accuse.setDisable(!playerService.standOnEndField());
                suspect.setDisable(!playerService.standOnRoomField());
            }
        });
        notificationService.showYourCardNotificationProperty().addListener((observable, oldValue, newValue) -> {
            show.setDisable(false);
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
        roll.setOnAction(event -> {
            RollTask task = new RollTask(userService);
            task.setOnSucceeded(e -> roll.setDisable(true));
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        suspect.setOnAction(event -> suspectPopOver.show(suspect));
        show.setOnAction(event -> cardPopOver.show(show));
        accuse.setOnAction(event -> accusePopOver.show(accuse));
        next.setOnAction(event -> {
            NextTask task = new NextTask(userService);
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
        quit.setOnAction(event -> {
            QuitTask task = new QuitTask(userService);
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
    }

    private void initButtons() {
        if (notificationService.getCurrentPlayerNotification() != null) {
            toggleButtons(!userService.getUsername().equals(notificationService.getCurrentPlayerNotification().getCurrent()));
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
        next.setDisable(value);
    }
}
