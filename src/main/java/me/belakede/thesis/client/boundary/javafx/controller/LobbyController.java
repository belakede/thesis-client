package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.GameDetailsPane;
import me.belakede.thesis.client.boundary.javafx.control.controller.GamesPaneController;
import me.belakede.thesis.client.boundary.javafx.control.controller.PlayersPaneController;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.server.game.domain.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class LobbyController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    private final Map<GameSummary, GameDetailsPane> cache = new HashMap<>();
    private final ObjectProperty<Optional<GameSummary>> runningGame = new SimpleObjectProperty<>();

    private final GameService gameService;
    private final GamesPaneController gamesPaneController;
    private final PlayersPaneController playersPaneController;

    @FXML
    private VBox parent;
    @FXML
    private VBox content;

    @Autowired
    public LobbyController(GameService gameService, GamesPaneController gamesPaneController, PlayersPaneController playersPaneController) {
        this.gameService = gameService;
        this.gamesPaneController = gamesPaneController;
        this.playersPaneController = playersPaneController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupBindings();
        hookupChangeListeners();
    }

    private void setupBindings() {
        gamesPaneController.playersProperty().bind(playersPaneController.playersProperty());
        runningGame.bind(Bindings.createObjectBinding(() -> gamesPaneController.gamesProperty().stream().filter(g -> Status.IN_PROGRESS.equals(g.getStatus())).findFirst(), gamesPaneController.gamesProperty()));
    }

    private void hookupChangeListeners() {
        gamesPaneController.selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                content.getChildren().clear();
                if (!cache.containsKey(newValue)) {
                    cache.put(newValue, new GameDetailsPane(newValue, () -> gamesPaneController.remove(newValue), () -> {
                        runningGame.unbind();
                        runningGame.setValue(Optional.ofNullable(newValue));
                    }));
                }
                content.getChildren().add(cache.get(newValue));
            }
        });
        runningGame.addListener((observable, oldValue, newValue) -> {
            if (newValue.isPresent()) {
                gameService.setGameId(newValue.get().getId());
                LOGGER.info("Running game changed to {}", gameService.getGameId());
                gameService.setRoomId(newValue.get().getRoomId());
                LOGGER.info("Room ID is: {}", gameService.getRoomId());
                gameService.setPlayers(newValue.get().getPlayers());
                LOGGER.info("Players: {}", gameService.getPlayers());
                hide();
            }
        });
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.setOnFinished(event -> parent.setVisible(false));
        transition.play();
    }

}
