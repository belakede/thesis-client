package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.ChatPane;
import me.belakede.thesis.client.boundary.javafx.control.GamePane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryPane;
import me.belakede.thesis.client.boundary.javafx.control.NotePane;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.server.game.response.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

@Controller
public class GameController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;
    private final NotificationService notificationService;
    private long numberOfSnapshots;

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
    public GameController(GameService gameService, NotificationService notificationService) {
        this.gameService = gameService;
        this.notificationService = notificationService;
        this.numberOfSnapshots = 0;
    }

    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        createDirectory();
    }

    private void createDirectory() {
        try {
            Files.createDirectories(Paths.get("videos/" + gameService.getRoomId()));
        } catch (IOException e) {
            LOGGER.warn("Can't create videos directory");
        }
    }

    private void hookupChangeListeners() {
        notificationService.notificationsProperty().addListener((ListChangeListener.Change<? extends Notification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(o -> saveAsPng());
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

    public void saveAsPng() {
        Platform.runLater(() -> {
            WritableImage image = parent.snapshot(new SnapshotParameters(), null);
            File file = new File(String.format("videos/%s/snapshot_%06d.png", gameService.getRoomId(), numberOfSnapshots));
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                numberOfSnapshots++;
            } catch (IOException e) {
                LOGGER.warn("Can't create png file");
            }
        });
    }

}
