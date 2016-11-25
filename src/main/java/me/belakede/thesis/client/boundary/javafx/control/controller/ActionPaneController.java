package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import me.belakede.thesis.client.boundary.javafx.control.AccusePane;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
import me.belakede.thesis.client.boundary.javafx.control.SuspectPane;
import me.belakede.thesis.client.boundary.javafx.task.NextTask;
import me.belakede.thesis.client.boundary.javafx.task.QuitTask;
import me.belakede.thesis.client.boundary.javafx.task.RollTask;
import me.belakede.thesis.client.service.*;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.PairOfDice;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.game.field.FieldType;
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
            initButtons();
        });
        rollService.rollsProperty().addListener((ListChangeListener.Change<? extends PairOfDice> change) -> {
            if (playerService.isCurrent()) {
                while (change.next()) {
                    roll.setDisable(true);
                    accuse.setDisable(!playerService.standOnEndField());
                    suspect.setDisable(!playerService.standOnRoomField());
                }
            }
        });
        positionService.positionsProperty().addListener((Change<? extends Figurine, ? extends Field> change) -> {
            if (playerService.isCurrent() && change.wasAdded() && change.getKey().equals(playerService.getFigurine())) {
                accuse.setDisable(!FieldType.END.equals((change.getValueAdded().getFieldType())));
                suspect.setDisable(!FieldType.ROOM.equals((change.getValueAdded().getFieldType())));
            }
        });
        notificationService.suspicionNotificationProperty().addListener((observable, oldValue, newValue) -> {
            if (playerService.isCurrent()) {
                roll.setDisable(true);
                suspect.setDisable(true);
                accuse.setDisable(true);
            }
        });
        notificationService.accusationNotificationProperty().addListener((observable, oldValue, newValue) -> {
            if (playerService.isCurrent()) {
                roll.setDisable(true);
                suspect.setDisable(true);
                accuse.setDisable(true);
            }
        });
        notificationService.cardNotificationProperty().addListener((observable, oldValue, newValue) -> {
            if (playerService.isCurrent()) {
                roll.setDisable(true);
                suspect.setDisable(true);
                accuse.setDisable(true);
            }
        });
        playerService.lastActionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case ACCUSE:
                        accuse.setDisable(true);
                    case SUSPECT:
                        suspect.setDisable(true);
                    case MOVE:
                    case ROLL:
                        roll.setDisable(true);
                }
            }
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
        suspectPopOver = new PopOver(new SuspectPane());
        setupPopOver(suspectPopOver);
    }

    private void setupAccusePopOver() {
        accusePopOver = new PopOver(new AccusePane());
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
        if (!playerService.isCurrent()) {
            toggleButtons(!playerService.isCurrent());
        } else {
            roll.setDisable(false);
            accuse.setDisable(!playerService.standOnEndField());
            suspect.setDisable(!playerService.standOnRoomField());
            next.setDisable(false);
            if (playerService.getLastAction() != null) {
                switch (playerService.getLastAction()) {
                    case ACCUSE:
                        accuse.setDisable(!playerService.standOnEndField());
                    case SHOW:
                    case SUSPECT:
                        suspect.setDisable(!playerService.standOnRoomField());
                    case MOVE:
                    case ROLL:
                        roll.setDisable(true);
                }
            }
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
        accuse.setDisable(value);
        next.setDisable(value);
    }
}
