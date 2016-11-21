package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.BoardPane;
import me.belakede.thesis.client.boundary.javafx.control.SideBar;
import me.belakede.thesis.client.service.NotificationService;
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

    @FXML
    private NotificationPane notificationPane;
    @FXML
    private SideBar sideBar;
    @FXML
    private BoardPane boardPane;

    @Autowired
    public GamePaneController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        transformBoard();
    }

    private void hookupChangeListeners() {
        sideBar.bindSize(boardPane.widthProperty());
        sideBar.getRotates().addListener((ListChangeListener.Change<? extends Rotate> change) -> {
            while (change.next()) {
                boardPane.getTransforms().clear();
                boardPane.getTransforms().addAll(change.getAddedSubList());
            }
        });
        notificationService.pairOfDiceNotificationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                notificationPane.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CUBE));
                notificationPane.setText(newValue.toString());
                notificationPane.setOnShown(event -> hideNotificationPane());
                notificationPane.show();
            });
        });
        notificationService.figurineNotificationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                notificationPane.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.MAP_MARKER));
                notificationPane.setText(newValue.toString());
                notificationPane.setOnShown(event -> hideNotificationPane());
                notificationPane.show();
            });
        });
        notificationService.currentPlayerNotificationProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                notificationPane.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.USER));
                notificationPane.setText(newValue.toString());
                notificationPane.setOnShown(event -> hideNotificationPane());
                notificationPane.show();
            });
        });
    }

    private void hideNotificationPane() {
        final Timeline timeline = new Timeline();
        timeline.setDelay(Duration.seconds(3));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.seconds(1), (e) -> notificationPane.hide()));
        timeline.play();
    }

    private void transformBoard() {
        boardPane.getTransforms().addAll(sideBar.getRotates());
    }
}
