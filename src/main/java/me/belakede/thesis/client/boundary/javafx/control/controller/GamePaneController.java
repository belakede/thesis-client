package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.BoardPane;
import me.belakede.thesis.client.boundary.javafx.control.SideBar;
import me.belakede.thesis.client.boundary.javafx.controller.LoungeController;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.server.game.response.CardNotification;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class GamePaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamePaneController.class);
    private final NotificationService notificationService;
    private final LoungeController loungeController;

    @FXML
    private NotificationPane notificationPane;
    @FXML
    private SideBar sideBar;
    @FXML
    private BorderPane boardPaneHolder;

    @Autowired
    public GamePaneController(NotificationService notificationService, LoungeController loungeController) {
        this.notificationService = notificationService;
        this.loungeController = loungeController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        initBoardPane();
    }

    private void initBoardPane() {
        if (loungeController.getBoardPane() != null) {
            setBoardPane(loungeController.getBoardPane());
        }
    }

    public void setBoardPane(BoardPane boardPane) {
        Platform.runLater(() -> boardPaneHolder.setCenter(boardPane));
        transformBoard(boardPane);
        bindSideBarAndBoardPane(boardPane);
    }

    private void bindSideBarAndBoardPane(BoardPane boardPane) {
        sideBar.bindSize(boardPane.widthProperty());
        sideBar.getRotates().addListener((ListChangeListener.Change<? extends Rotate> change) -> {
            while (change.next()) {
                boardPane.getTransforms().clear();
                boardPane.getTransforms().addAll(change.getAddedSubList());
            }
        });
    }

    private void hookupChangeListeners() {
        loungeController.boardPaneProperty().addListener((observable, oldValue, newValue) -> {
            setBoardPane(newValue);
        });
        notificationService.pairOfDiceNotificationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.CUBE), newValue.toString());
            });
        });
        notificationService.figurineNotificationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.MAP_MARKER), newValue.toString());
            });
        });
        notificationService.currentPlayerNotificationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.USER), newValue.toString());
            });
        });
        notificationService.cardNotificationsProperty().addListener((ListChangeListener.Change<? extends CardNotification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(newValue -> {
                        Platform.runLater(() -> {
                            showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.PHOTO), newValue.toString());
                        });
                    });
                }
            }
        });
    }

    private void showNotification(Glyph fontAwesome, String value) {
        notificationPane.setGraphic(fontAwesome);
        notificationPane.setText(value);
        notificationPane.setOnShown(event -> hideNotificationPane());
        notificationPane.show();
    }

    private void hideNotificationPane() {
        final Timeline timeline = new Timeline();
        timeline.setDelay(Duration.seconds(3));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.seconds(1), (e) -> notificationPane.hide()));
        timeline.play();
    }

    private void transformBoard(BoardPane boardPane) {
        boardPane.getTransforms().addAll(sideBar.getRotates());
    }
}
