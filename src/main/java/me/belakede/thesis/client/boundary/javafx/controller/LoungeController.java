package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.belakede.thesis.client.service.GameFlowService;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.server.game.response.PlayerJoinedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoungeController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoungeController.class);

    private final MapProperty<String, VBox> playerBoxes = new SimpleMapProperty<>();
    private final UserService userService;
    private final GameService gameService;
    private final GameFlowService gameFlowService;
    private final NotificationService notificationService;
    @FXML
    private StackPane sorryPane;
    @FXML
    private HBox playerContainer;

    @Autowired
    public LoungeController(UserService userService, GameService gameService, GameFlowService gameFlowService, NotificationService notificationService) {
        this.userService = userService;
        this.gameService = gameService;
        this.gameFlowService = gameFlowService;
        this.notificationService = notificationService;
    }

    public ObservableMap<String, VBox> getPlayerBoxes() {
        return playerBoxes.get();
    }

    public void setPlayerBoxes(ObservableMap<String, VBox> playerBoxes) {
        this.playerBoxes.set(playerBoxes);
    }

    public MapProperty<String, VBox> playerBoxesProperty() {
        return playerBoxes;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPlayerBoxes(FXCollections.observableHashMap());
        uploadPlayerBoxes();
        if (isPartOfGame()) {
            hookupChangeListeners();
            openChannels();
        } else {
            displaySorryPane();
        }
    }

    private void uploadPlayerBoxes() {
        gameService.getPlayers().entrySet().forEach(entry -> {
            playerBoxes.put(entry.getValue(), createVBoxForUser(entry.getValue(), entry.getKey()));
        });
    }

    private boolean isPartOfGame() {
        return gameService.getPlayers().containsValue(userService.getUsername());
    }

    private void displaySorryPane() {
        sorryPane.setVisible(true);
    }

    private void hookupChangeListeners() {
        playerBoxesProperty().addListener(new MapChangeListener<String, VBox>() {
            @Override
            public void onChanged(Change<? extends String, ? extends VBox> change) {
                if (change.wasAdded()) {
                    playerContainer.getChildren().add(change.getValueAdded());
                }
            }
        });
        notificationService.playerJoinedNotificationsProperty().addListener((ListChangeListener.Change<? extends PlayerJoinedNotification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(notification -> {
                        getPlayerBoxes().get(notification.getUser()).getStyleClass().add("online");
                    });
                }
            }
        });
    }

    private void openChannels() {
        gameFlowService.openChannels();
    }

    private VBox createVBoxForUser(String username, Suspect figurine) {
        VBox result = new VBox();
        result.getStyleClass().addAll("player", figurine.name().toLowerCase());
        if (userService.getUsername().equals(username)) {
            result.getStyleClass().add("online");
        }
        result.getChildren().addAll(createIconForUser(), createTextForUser(username));
        return result;
    }

    private VBox createIconForUser() {
        VBox result = new VBox();
        result.getStyleClass().add("icon");
        return result;
    }

    private Text createTextForUser(String username) {
        return new Text(username);
    }

}
