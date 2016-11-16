package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Suspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class LoungeController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoungeController.class);

    private final UserService userService;
    private final GameService gameService;
    @FXML
    private StackPane sorryPane;
    @FXML
    private HBox playerContainer;

    @Autowired
    public LoungeController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String username = userService.getUsername();
        Map<Suspect, String> players = gameService.getPlayers();
        players.entrySet().forEach(entry -> {
            LOGGER.info("waiting for user: {}", entry.getValue());
            VBox playerBox = new VBox();
            playerBox.getStyleClass().addAll("player", entry.getKey().name().toLowerCase());
            if (username.equals(entry.getValue())) {
                LOGGER.info("{} online", entry.getValue());
                playerBox.getStyleClass().add("online");
            }
            VBox iconBox = new VBox();
            iconBox.getStyleClass().add("icon");
            Text text = new Text(entry.getValue());

            playerBox.getChildren().addAll(iconBox, text);
            playerContainer.getChildren().add(playerBox);
        });
        if (!players.containsValue(username)) {
            sorryPane.setVisible(true);
        }
    }
}
